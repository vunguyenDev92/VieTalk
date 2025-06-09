package com.android.internship.presentation.screens.chat

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.internship.domain.repository.AuthRepository
import com.android.internship.domain.usecase.AddTypingUseCase
import com.android.internship.domain.usecase.GetAllUsersInfoUseCase
import com.android.internship.domain.usecase.GetRoomUseCase
import com.android.internship.domain.usecase.GetUserRoomUseCase
import com.android.internship.domain.usecase.ObserveMessagesUseCase
import com.android.internship.domain.usecase.ObserveUserRoomDetailsUseCase
import com.android.internship.domain.usecase.SeenMessageUseCase
import com.android.internship.domain.usecase.SendMessagesUseCase
import com.android.internship.presentation.components.MessageState
import com.android.internship.presentation.components.utils.processMessagesToItems
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ChatViewModel(
    savedStateHandle: SavedStateHandle,
    authRepository: AuthRepository,
    private val getRoomUseCase: GetRoomUseCase,
    private val getUserRoomUseCase: GetUserRoomUseCase,
    private val getUserInfoUseCase: GetAllUsersInfoUseCase,
    private val observeMessagesUseCase: ObserveMessagesUseCase,
    private val observeUserRoomDetailsUseCase: ObserveUserRoomDetailsUseCase,
    private val sendMessageUseCase: SendMessagesUseCase,
    private val seenMessageUseCase: SeenMessageUseCase,
    private val addTypingUseCase: AddTypingUseCase,
) : ViewModel() {

    private val roomId: String = checkNotNull(savedStateHandle["roomId"])
    private val currentUserId: String = checkNotNull(authRepository.getCurrentUserId())

    private val _uiState = MutableStateFlow(MessageState(currentUserId = currentUserId))
    val uiState = _uiState.asStateFlow()

    private val _messageText = MutableStateFlow("")
    val messageText = _messageText.asStateFlow()

    init {
        loadChatData()
    }

    private fun loadChatData() {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            try {
                val room = getRoomUseCase(roomId)
                if (room == null) {
                    _uiState.update { it.copy(isLoading = false, errorMessage = "Room not found.") }
                    return@launch
                }

                val initialUserRooms = getUserRoomUseCase(roomId)
                val userIdsInRoom = initialUserRooms.map { it.uid }
                val usersInRoom = getUserInfoUseCase(userIdsInRoom)
                val userMap = usersInRoom.associateBy { it.uid }

                combine(
                    observeMessagesUseCase(roomId),
                    observeUserRoomDetailsUseCase(roomId),
                    _uiState.map { it.seenByExpandedState }.distinctUntilChanged(),
                ) { messages, userRoomDetails, seenByState ->

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
                            messages = processedItems,
                            userMap = userMap,
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
        if (text.isNotBlank()) {
            addTypingUseCase(roomId)
        }
    }

    fun sendMessage() {
        val content = _messageText.value.trim()
        if (content.isNotBlank()) {
            sendMessageUseCase(content = content, rid = roomId)
            _messageText.value = ""
        }
    }

    fun toggleSeenByVisibility(messageId: String) {
        val currentMap = _uiState.value.seenByExpandedState
        val newMap = currentMap.toMutableMap()
        newMap[messageId] = !(currentMap[messageId] ?: false)
        _uiState.update { it.copy(seenByExpandedState = newMap) }
    }

    fun markAsSeen(messageId: String) {
        viewModelScope.launch {
            seenMessageUseCase(rid = roomId, mid = messageId)
        }
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}
