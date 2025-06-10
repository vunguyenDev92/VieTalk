package com.android.internship.presentation.screens.chat

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.internship.data.model.Message
import com.android.internship.data.model.Room
import com.android.internship.domain.repository.AuthRepository
import com.android.internship.domain.usecase.AddTypingUseCase
import com.android.internship.domain.usecase.GetAllUsersInfoUseCase
import com.android.internship.domain.usecase.GetRoomUseCase
import com.android.internship.domain.usecase.ObserveMessagesUseCase
import com.android.internship.domain.usecase.ObserveUserRoomDetailsUseCase
import com.android.internship.domain.usecase.SeenMessageUseCase
import com.android.internship.domain.usecase.SendMessagesUseCase
import com.android.internship.presentation.components.MessageState
import com.android.internship.presentation.components.utils.processMessagesToItems
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ChatViewModel(
    savedStateHandle: SavedStateHandle,
    authRepository: AuthRepository,
    private val getRoomUseCase: GetRoomUseCase,
    private val getUserInfoUseCase: GetAllUsersInfoUseCase,
    private val observeMessagesUseCase: ObserveMessagesUseCase,
    private val observeUserRoomDetailsUseCase: ObserveUserRoomDetailsUseCase,
    private val sendMessageUseCase: SendMessagesUseCase,
    private val seenMessageUseCase: SeenMessageUseCase,
    private val addTypingUseCase: AddTypingUseCase,
) : ViewModel() {

    private val currentUserId: String = checkNotNull(authRepository.getCurrentUserId())
    private val roomId: String = checkNotNull(savedStateHandle["rid"])
    private val _uiState = MutableStateFlow(MessageState(currentUserId = currentUserId))
    val uiState = _uiState.asStateFlow()

    private val _messageText = MutableStateFlow("")
    val messageText = _messageText.asStateFlow()
    private var typingTimeoutJob: Job? = null

    init {
        loadChatData()
    }

    private fun loadChatData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val room = getRoomUseCase(roomId)
                if (room == null) {
                    _uiState.update { it.copy(isLoading = false, errorMessage = "Room not found.") }
                    return@launch
                }
                observeCombinedData(room)
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = e.message) }
            }
        }
    }

    private fun observeCombinedData(room: Room) = viewModelScope.launch {
        val userRoomDetailsFlow = observeUserRoomDetailsUseCase(roomId)

        val usersInRoomFlow = userRoomDetailsFlow.flatMapLatest { userRooms ->
            val userIds = userRooms.map { it.uid }.distinct()
            if (userIds.isNotEmpty()) {
                flow { emit(getUserInfoUseCase(userIds)) }
            } else {
                flowOf(emptyList())
            }
        }.distinctUntilChanged()

        combine(
            usersInRoomFlow,
            observeMessagesUseCase(roomId),
            userRoomDetailsFlow,
            _uiState.map { it.expandedMessageId }.distinctUntilChanged(),
        ) { usersInRoom, messages, userRoomDetails, expandedId ->

            val totalMemberCount = usersInRoom.size
            val isTrueGroup = room.isGroup && totalMemberCount > 2
            val otherUser = if (!isTrueGroup) usersInRoom.find { it.uid != currentUserId } else null

            val topBarTitle = if (isTrueGroup) {
                room.name ?: "Group Chat"
            } else {
                otherUser?.username ?: room.name ?: "Chat"
            }

            val isPeerActive = otherUser?.let {
                val lastActiveMillis = it.lastActiveTime.toLongOrNull() ?: 0L
                val isActive = (System.currentTimeMillis() - lastActiveMillis) <= (5 * 60 * 1000)

                Log.d("ChatViewModel", "lastActiveTime: $lastActiveMillis, isPeerActive: $isActive")

                isActive
            } ?: false

            val topBarSubtitle = if (isTrueGroup) {
                "$totalMemberCount members"
            } else {
                if (isPeerActive) "Active Now" else "Offline"
            }

            val topBarAvatarUrls = if (isTrueGroup) {
                usersInRoom
                    .filter { it.uid != currentUserId }
                    .mapNotNull { it.avatar }
                    .shuffled()
                    .take(2)
            } else {
                listOfNotNull(otherUser?.avatar)
            }

            val roomForProcessing = room.copy(name = topBarTitle, avatar = topBarAvatarUrls.firstOrNull())

            val processedItems = processMessagesToItems(
                messages = messages,
                room = roomForProcessing,
                usersInRoom = usersInRoom,
                userRoomDetails = userRoomDetails,
                currentUserId = currentUserId,
                expandedMessageId = expandedId,
            )

            val currentUser = usersInRoom.find { it.uid == currentUserId }

            _uiState.update {
                it.copy(
                    isLoading = false,
                    room = roomForProcessing,
                    messages = processedItems,
                    userMap = usersInRoom.associateBy { u -> u.uid },
                    currentUser = currentUser,
                    topBarTitle = topBarTitle,
                    topBarSubtitle = topBarSubtitle,
                    topBarAvatarUrls = topBarAvatarUrls,
                    isPeerActive = isPeerActive,
                )
            }
        }.catch { e -> _uiState.update { it.copy(isLoading = false, errorMessage = e.message) } }.collect()
    }

    fun onMessageChange(text: String) {
        _messageText.value = text
        typingTimeoutJob?.cancel()
        if (text.isNotBlank()) {
            addTypingUseCase(rid = roomId, isTyping = true)
            typingTimeoutJob = viewModelScope.launch {
                delay(3000L)
                addTypingUseCase(rid = roomId, isTyping = false)
            }
        } else {
            addTypingUseCase(rid = roomId, isTyping = false)
        }
    }

    fun sendMessage() {
        val content = _messageText.value.trim()
        if (content.isNotBlank()) {
            typingTimeoutJob?.cancel()
            sendMessageUseCase(content = content, rid = roomId)
            _messageText.value = ""
            addTypingUseCase(rid = roomId, isTyping = false)
        }
    }

    fun toggleSeenByVisibility(messageId: String) {
        val currentExpandedId = _uiState.value.expandedMessageId
        val newExpandedId = if (currentExpandedId == messageId) null else messageId
        _uiState.update { it.copy(expandedMessageId = newExpandedId) }
    }

    fun markAsSeen(message: Message) {
        viewModelScope.launch {
            seenMessageUseCase(rid = roomId, message = message)
        }
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}
