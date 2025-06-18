package com.android.internship.presentation.screens.chat

import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.internship.data.model.Room
import com.android.internship.domain.repository.AuthRepository
import com.android.internship.domain.usecase.GetAllUsersInRoomUseCase
import com.android.internship.domain.usecase.GetLatestLocalMessageUseCase
import com.android.internship.domain.usecase.GetOlderMessagesUseCase
import com.android.internship.domain.usecase.GetRoomsUseCase
import com.android.internship.domain.usecase.ObserveMessagesUseCase
import com.android.internship.domain.usecase.ObserveNewMessagesUseCase
import com.android.internship.domain.usecase.ObserveSingleRoomUseCase
import com.android.internship.domain.usecase.ObserveUserRoomDetailsUseCase
import com.android.internship.domain.usecase.SaveLocalMessagesUseCase
import com.android.internship.domain.usecase.SeenMessageUseCase
import com.android.internship.domain.usecase.SendMessagesUseCase
import com.android.internship.domain.usecase.UpdateActiveTimeUseCase
import com.android.internship.domain.usecase.UpdateTypingTimeUseCase
import com.android.internship.presentation.components.MessageState
import com.android.internship.presentation.components.utils.IConnectivityObserver
import com.android.internship.presentation.components.utils.processMessagesToItems
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ChatViewModel(
    savedStateHandle: SavedStateHandle,
    authRepository: AuthRepository,
    private val getRoomsUseCase: GetRoomsUseCase,
    private val observeMessagesUseCase: ObserveMessagesUseCase,
    private val observeUserRoomDetailsUseCase: ObserveUserRoomDetailsUseCase,
    private val sendMessageUseCase: SendMessagesUseCase,
    private val seenMessageUseCase: SeenMessageUseCase,
    private val addTypingUseCase: UpdateTypingTimeUseCase,
    private val updateActiveUserUseCase: UpdateActiveTimeUseCase,
    private val getAllUsersInRoomUseCase: GetAllUsersInRoomUseCase,
    private val getOlderMessagesUseCase: GetOlderMessagesUseCase,
    private val getLatestLocalMessageUseCase: GetLatestLocalMessageUseCase,
    private val saveLocalMessagesUseCase: SaveLocalMessagesUseCase,
    private val observeNewMessagesUseCase: ObserveNewMessagesUseCase,
    private val connectivityObserver: IConnectivityObserver,
    private val observeSingleRoomUseCase: ObserveSingleRoomUseCase,
) : ViewModel() {

    private val currentUserId: String = checkNotNull(authRepository.getCurrentUserId())
    private val roomId: String = checkNotNull(savedStateHandle["rid"])
    private val _uiState = MutableStateFlow(MessageState(currentUserId = currentUserId))
    val uiState = _uiState.asStateFlow()

    private val _messageText = MutableStateFlow(TextFieldValue(""))
    val messageText = _messageText.asStateFlow()
    private var typingTimeoutJob: Job? = null

    companion object {
        private const val INITIAL_UI_SIZE = 30
        private const val LOAD_MORE_UI_SIZE = 20
        private const val REMOTE_PAGING_SIZE = 20
    }

    @Suppress("ktlint:standard:backing-property-naming")
    private val _currentUIMessageCount = MutableStateFlow(INITIAL_UI_SIZE)
    private val currentUIMessageCount = _currentUIMessageCount.asStateFlow()

    init {
        observeNetworkStatus()
        loadInitialData()
        startPeriodicActiveUpdate()
        startBackgroundSync()
        observeRoomForAutoSeen()
    }

    private fun observeRoomForAutoSeen() {
        viewModelScope.launch {
            observeSingleRoomUseCase(roomId)
                .filterNotNull()
                .distinctUntilChangedBy { it.lastMessage.mid }
                .collect { room ->
                    val lastMessage = room.lastMessage
                    if (lastMessage.mid.isNotEmpty()) {
                        seenMessageUseCase(roomId, lastMessage.mid)
                    }
                }
        }
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            try {
                val room = getRoomsUseCase(listOf(roomId))?.firstOrNull()
                if (room == null) {
                    _uiState.update { it.copy(isLoading = false, errorMessage = "Room not found.") }
                    return@launch
                }

                val localMessages = observeMessagesUseCase(roomId).first()

                if (localMessages.size < INITIAL_UI_SIZE) {
                    val neededMessages = INITIAL_UI_SIZE - localMessages.size
                    val fetchSize = maxOf(neededMessages, REMOTE_PAGING_SIZE)
                    getOlderMessagesUseCase(roomId, fetchSize)

                    delay(500)
                }

                observeCombinedData(room)
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = e.message) }
            }
        }
    }

    private fun startBackgroundSync() {
        viewModelScope.launch {
            val latestMessage = getLatestLocalMessageUseCase(roomId)
            val latestMessageTime = latestMessage?.time?.toLongOrNull() ?: (System.currentTimeMillis() - 86400000)

            observeNewMessagesUseCase(roomId, latestMessageTime)
                .collect { newMessages ->
                    if (newMessages.isNotEmpty()) {
                        saveLocalMessagesUseCase(newMessages)
                    }
                }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
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
            currentUIMessageCount,
            _uiState.map { it.expandedMessageId }.distinctUntilChanged(),
        ) { usersInRoom, allMessages, userRoomDetails, uiMessageCount, expandedId ->

            val otherUser = if (!room.isGroup) usersInRoom.find { it.uid != currentUserId } else null
            val topBarTitle = if (room.isGroup) room.name ?: "Group Chat" else otherUser?.username ?: "Chat"
            val isPeerActive = otherUser?.let { (System.currentTimeMillis() - (it.lastActiveTime.toLongOrNull() ?: 0L)) <= (3 * 60 * 1000) } == true
            val topBarSubtitle = if (room.isGroup) {
                "${usersInRoom.size} members"
            } else if (isPeerActive) {
                "Active Now"
            } else {
                "Offline"
            }
            val topBarAvatarUrls = if (room.isGroup) usersInRoom.filter { it.uid != currentUserId }.mapNotNull { it.avatar }.take(2) else listOfNotNull(otherUser?.avatar)

            val messagesToShow = allMessages.takeLast(uiMessageCount)
            val canLoadMoreFromLocal = allMessages.size > uiMessageCount

            val processedItems = processMessagesToItems(
                messages = messagesToShow,
                room = room,
                usersInRoom = usersInRoom,
                userRoomDetails = userRoomDetails,
                currentUserId = currentUserId,
                expandedMessageId = expandedId,
            )
            val currentUser = usersInRoom.find { it.uid == currentUserId }

            val newState = _uiState.value.copy(
                isLoading = false,
                room = room,
                messages = processedItems,
                userMap = usersInRoom.associateBy { u -> u.uid },
                currentUser = currentUser,
                topBarTitle = topBarTitle,
                topBarSubtitle = topBarSubtitle,
                topBarAvatarUrls = topBarAvatarUrls,
                isPeerActive = isPeerActive,
                canLoadMore = canLoadMoreFromLocal,
            )

            if (_uiState.value != newState) {
                _uiState.update { newState }
            }
        }
            .distinctUntilChanged()
            .catch { e ->
                _uiState.update { it.copy(isLoading = false, errorMessage = "Error loading chat: ${e.message}") }
            }.collect()
    }

    fun loadMoreMessages() {
        if (_uiState.value.isLoadingMore || !_uiState.value.canLoadMore) {
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingMore = true) }

            try {
                val currentCount = _currentUIMessageCount.value
                val newCount = currentCount + LOAD_MORE_UI_SIZE

                // Get total messages available in local DB
                val totalLocalMessages = observeMessagesUseCase(roomId).first().size

                if (newCount >= totalLocalMessages - 10) {
                    backgroundFetchOlderMessages()
                }

                _currentUIMessageCount.value = newCount

                delay(200)
            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = "Failed to load older messages.") }
            } finally {
                _uiState.update { it.copy(isLoadingMore = false) }
            }
        }
    }

    private fun backgroundFetchOlderMessages() {
        viewModelScope.launch {
            try {
                getOlderMessagesUseCase(roomId, REMOTE_PAGING_SIZE)
            } catch (e: Exception) {
                // Handle error silently or update UI state if needed
            }
        }
    }

    private fun resetUIPaginationIfNeeded() {
        viewModelScope.launch {
            val totalMessages = observeMessagesUseCase(roomId).first().size
            val currentUICount = _currentUIMessageCount.value

            if (totalMessages > currentUICount && currentUICount < INITIAL_UI_SIZE + 10) {
                _currentUIMessageCount.value = minOf(totalMessages, INITIAL_UI_SIZE)
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
            val currentUser = _uiState.value.currentUser
            sendMessageUseCase(
                content = content,
                rid = roomId,
                senderName = currentUser?.username ?: "",
                senderAvatar = currentUser?.avatar,
            )
            _messageText.value = TextFieldValue("")
            addTypingUseCase(rid = roomId, isTyping = false)

            resetUIPaginationIfNeeded()
        }
    }

    fun toggleSeenByVisibility(messageId: String) {
        val newExpandedId = if (_uiState.value.expandedMessageId == messageId) null else messageId
        _uiState.update { it.copy(expandedMessageId = newExpandedId) }
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }

    fun refreshData() {
        if (!_uiState.value.isNetworkAvailable) {
            _uiState.update { it.copy(isRefreshing = true) }
            viewModelScope.launch {
                delay(5000)
                _uiState.update { it.copy(isRefreshing = false) }
            }
        }
    }

    private fun observeNetworkStatus() {
        viewModelScope.launch {
            connectivityObserver.observe().collect { status ->
                val isAvailable = status == IConnectivityObserver.Status.Available
                _uiState.update {
                    it.copy(
                        isNetworkAvailable = isAvailable,
                        isRefreshing = if (isAvailable) false else it.isRefreshing,
                    )
                }
            }
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
}
