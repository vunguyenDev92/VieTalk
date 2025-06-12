package com.android.internship.presentation.screens.chat

import android.util.Log
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.internship.data.model.Room
import com.android.internship.domain.repository.AuthRepository
import com.android.internship.domain.usecase.GetAllUsersInRoomUseCase
import com.android.internship.domain.usecase.GetAllUsersInfoUseCase
import com.android.internship.domain.usecase.GetMessagesUseCase
import com.android.internship.domain.usecase.GetRoomUseCase
import com.android.internship.domain.usecase.ObserveMessagesUseCase
import com.android.internship.domain.usecase.ObserveUserRoomDetailsUseCase
import com.android.internship.domain.usecase.SeenMessageUseCase
import com.android.internship.domain.usecase.SendMessagesUseCase
import com.android.internship.domain.usecase.UpdateActiveTimeUseCase
import com.android.internship.domain.usecase.UpdateTypingTimeUseCase
import com.android.internship.presentation.components.MessageState
import com.android.internship.presentation.components.utils.IConnectivityObserver
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
    private val addTypingUseCase: UpdateTypingTimeUseCase,
    private val updateActiveUserUseCase: UpdateActiveTimeUseCase,
    private val getAllUsersInRoomUseCase: GetAllUsersInRoomUseCase,
    private val connectivityObserver: IConnectivityObserver,
    getMessagesUseCase: GetMessagesUseCase,
) : ViewModel() {

    private val currentUserId: String = checkNotNull(authRepository.getCurrentUserId())
    private val roomId: String = checkNotNull(savedStateHandle["rid"])
    private val _uiState = MutableStateFlow(MessageState(currentUserId = currentUserId))
    val uiState = _uiState.asStateFlow()

    private val _messageText = MutableStateFlow(TextFieldValue(""))
    val messageText = _messageText.asStateFlow()
    private var typingTimeoutJob: Job? = null

    init {
        observeNetworkStatus()
        loadChatData()
        startPeriodicActiveUpdate()
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
                flow { emit(getAllUsersInRoomUseCase(userIds)) }
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
            val isTrueGroup = usersInRoom.size > 2
            val otherUser = if (!isTrueGroup) usersInRoom.find { it.uid != currentUserId } else null

            val topBarTitle = if (isTrueGroup) {
                room.name ?: "Group Chat"
            } else {
                otherUser?.username ?: room.name ?: "Chat"
            }

            Log.d("DEBUG", " usersInRoom size: ${usersInRoom.size}")
            Log.d("DEBUG", "messages size: ${messages.size}")
            Log.d("DEBUG", "userRoomDetails size: ${userRoomDetails.size}")

            val isPeerActive = otherUser?.let {
                (System.currentTimeMillis() - (it.lastActiveTime.toLongOrNull() ?: 0L)) <= (3 * 60 * 1000)
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

    private fun observeNetworkStatus() {
        viewModelScope.launch {
            connectivityObserver.observe().collect { status ->
                val isAvailable = status == IConnectivityObserver.Status.Available
                _uiState.update {
                    it.copy(
                        isNetworkAvailable = isAvailable,
                        errorMessage = if (isAvailable) null else it.errorMessage,
                        isRefreshing = if (isAvailable) false else it.isRefreshing,
                    )
                }
            }
        }
    }

    fun refreshData() {
        if (!uiState.value.isNetworkAvailable) {
            _uiState.update { it.copy(isRefreshing = true) }
        }
    }

    private fun startPeriodicActiveUpdate() {
        viewModelScope.launch {
            while (true) {
                updateActiveUserUseCase()
                delay(4 * 60 * 1000L)
            }
        }
    }

    fun onMessageChange(textFieldValue: TextFieldValue) {
        _messageText.value = textFieldValue
        typingTimeoutJob?.cancel()
        if (textFieldValue.text.isNotBlank()) {
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
        val content = _messageText.value.text.trim()
        if (content.isNotBlank()) {
            typingTimeoutJob?.cancel()
            sendMessageUseCase(content = content, rid = roomId)
            _messageText.value = TextFieldValue("")
            addTypingUseCase(rid = roomId, isTyping = false)
        }
    }

    fun toggleSeenByVisibility(messageId: String) {
        val currentExpandedId = _uiState.value.expandedMessageId
        val newExpandedId = if (currentExpandedId == messageId) null else messageId
        _uiState.update { it.copy(expandedMessageId = newExpandedId) }
    }

    fun markAsSeen(messageId: String) {
        viewModelScope.launch {
            seenMessageUseCase(rid = roomId, lastSeenMessageId = messageId)
        }
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}
