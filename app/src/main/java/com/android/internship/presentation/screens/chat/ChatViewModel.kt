package com.android.internship.presentation.screens.chat

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.internship.data.model.Message
import com.android.internship.domain.repository.AuthRepository
import com.android.internship.domain.usecase.AddTypingUseCase
import com.android.internship.domain.usecase.GetAllUsersInfoUseCase
import com.android.internship.domain.usecase.GetRoomUseCase
import com.android.internship.domain.usecase.GetUserRoomUseCase
import com.android.internship.domain.usecase.ObserveMessagesUseCase
import com.android.internship.domain.usecase.ObserveUserRoomDetailsUseCase
import com.android.internship.domain.usecase.SeenMessageUseCase
import com.android.internship.domain.usecase.SendMessagesUseCase
import com.android.internship.domain.usecase.StopTypingUseCase
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
    private val getUserRoomUseCase: GetUserRoomUseCase, // Không cần UseCase này nữa
    private val observeMessagesUseCase: ObserveMessagesUseCase,
    private val observeUserRoomDetailsUseCase: ObserveUserRoomDetailsUseCase,
    private val sendMessageUseCase: SendMessagesUseCase,
    private val seenMessageUseCase: SeenMessageUseCase,
    private val addTypingUseCase: AddTypingUseCase,
    private val stopTypingUseCase: StopTypingUseCase,
) : ViewModel() {

    private val currentUserId: String = checkNotNull(authRepository.getCurrentUserId())
    private val roomId: String = checkNotNull(savedStateHandle["rid"])
    private val _uiState = MutableStateFlow(MessageState(currentUserId = currentUserId))
    val uiState = _uiState.asStateFlow()

    private val _messageText = MutableStateFlow("")
    val messageText = _messageText.asStateFlow()

    private var typingJob: Job? = null

    init {
        loadChatData()
    }

    private fun loadChatData() {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            try {
                // Biến lời gọi suspend thành Flow để đưa vào combine
                val roomFlow = flow { emit(getRoomUseCase(roomId)) }

                // Luồng chính lắng nghe các thay đổi của UserRoom
                val userRoomDetailsFlow = observeUserRoomDetailsUseCase(roomId)

                // Sử dụng flatMapLatest để lấy thông tin user MỖI KHI userRoomDetails thay đổi
                val usersInRoomFlow = userRoomDetailsFlow.flatMapLatest { userRooms ->
                    val userIds = userRooms.map { it.uid }.distinct()
                    if (userIds.isNotEmpty()) {
                        flow { emit(getUserInfoUseCase(userIds)) }
                    } else {
                        flowOf(emptyList())
                    }
                }.distinctUntilChanged()

                combine(
                    roomFlow,
                    usersInRoomFlow,
                    observeMessagesUseCase(roomId),
                    userRoomDetailsFlow,
                    _uiState.map { it.seenByExpandedState }.distinctUntilChanged(),
                ) { room, usersInRoom, messages, userRoomDetails, seenByState ->

                    if (room == null) {
                        _uiState.update { it.copy(isLoading = false, errorMessage = "Room not found.") }
                        return@combine
                    }

                    val processedItems = processMessagesToItems(
                        messages = messages,
                        room = room,
                        usersInRoom = usersInRoom,
                        userRoomDetails = userRoomDetails,
                        currentUserId = currentUserId,
                        seenByExpandedState = seenByState,
                    )

                    _uiState.update { currentState ->
                        currentState.copy(
                            isLoading = false,
                            room = room,
                            messages = processedItems, // Khớp với MessageState
                            userMap = usersInRoom.associateBy { it.uid },
                        )
                    }
                }.catch { e ->
                    _uiState.update { it.copy(isLoading = false, errorMessage = e.message) }
                }.collect()
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = e.message) }
            }
        }
    }

    fun onMessageChange(text: String) {
        _messageText.value = text
        typingJob?.cancel()
        if (text.isNotBlank()) {
            addTypingUseCase(roomId)
            typingJob = viewModelScope.launch {
                delay(3000L)
                stopTypingUseCase(roomId)
            }
        } else {
            stopTypingUseCase(roomId)
        }
    }

    fun sendMessage() {
        val content = _messageText.value.trim()
        if (content.isNotBlank()) {
            typingJob?.cancel()
            sendMessageUseCase(content = content, rid = roomId)
            _messageText.value = ""
            stopTypingUseCase(roomId)
        }
    }

    fun toggleSeenByVisibility(messageId: String) {
        val currentMap = _uiState.value.seenByExpandedState
        val newMap = currentMap.toMutableMap()
        newMap[messageId] = !(currentMap[messageId] ?: false)
        _uiState.update { it.copy(seenByExpandedState = newMap) }
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
