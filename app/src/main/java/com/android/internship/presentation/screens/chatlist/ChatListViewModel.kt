package com.android.internship.presentation.screens.chatlist

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.android.internship.di.AppContainer
import com.android.internship.domain.usecase.GetActiveUserUseCase
import com.android.internship.domain.usecase.GetAllUsersInfoUseCase
import com.android.internship.domain.usecase.GetRoomsUseCase
import com.android.internship.domain.usecase.GetUserRoomForRoomUseCase
import com.android.internship.domain.usecase.GetUserRoomForUserUseCase
import com.android.internship.domain.usecase.LogoutUserCase
import com.android.internship.domain.usecase.ObserveMessagesUseCase
import com.android.internship.presentation.utils.FormatTimeStamp
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ChatListViewModel(
    private val getUserRoomForUserUseCase: GetUserRoomForUserUseCase,
    private val getUserRoomForRoomUseCase: GetUserRoomForRoomUseCase,
    private val getRoomsUseCase: GetRoomsUseCase,
    private val getAllUsersInfoUseCase: GetAllUsersInfoUseCase,
    private val getActiveUserUseCase: GetActiveUserUseCase,
    private val logoutUserCase: LogoutUserCase,
    private val observeMessagesUseCase: ObserveMessagesUseCase,
    private val currentUserId: String?,
) : ViewModel() {
    private val _state = MutableStateFlow(ChatListState())
    val state = _state.asStateFlow()

    init {
        loadUserRooms()
    }

    private fun loadUserRooms() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            try {
                val chatRoomItems = mutableListOf<ChatListState.ChatRoomItemState>()
                val chatUserItems = mutableListOf<ChatListState.ChatUserItemState>()
                val userRooms = getUserRoomForUserUseCase()
                val rooms = if (userRooms.isNotEmpty()) {
                    getRoomsUseCase(userRooms.map { it.rid })
                } else {
                    emptyList()
                }
                val users = getAllUsersInfoUseCase()
                val userNoRoom = users.toMutableList()
                userNoRoom.removeIf { it.uid == currentUserId }

                for (userRoom in userRooms) {
                    val room = rooms?.find { it.rid == userRoom.rid }
                    val message = room?.lastMessage
                    val userRoomForRoom = getUserRoomForRoomUseCase(userRoom.rid)

                    if (room != null) {
                        val isActive = if (room.isGroup == false) {
                            userRoomForRoom.find { it.uid != currentUserId }?.uid?.let { uid ->
                                userNoRoom.removeIf { it.uid == uid }
                                getActiveUserUseCase(uid)
                            } == true
                        } else {
                            false
                        }

                        val name = if (room.isGroup == true) {
                            room.name.toString()
                        } else {
                            users.find {
                                it.uid == userRoomForRoom.find {
                                    it.uid != currentUserId
                                }?.uid
                            }?.username.toString()
                        }

                        val memberAvatar = users.map { user ->
                            if (userRoomForRoom.find { it.uid == user.uid && it.uid != currentUserId } != null) {
                                user.avatar.toString()
                            } else {
                                ""
                            }
                        }.filter { it.isNotEmpty() }

                        chatRoomItems.add(
                            ChatListState.ChatRoomItemState(
                                isGroupChat = room.isGroup,
                                isActive = isActive,
                                id = room.rid,
                                name = name,
                                memberAvatars = memberAvatar,
                                lastMessage = message?.content ?: "",
                                lastMessageTime = FormatTimeStamp.messageTimeFormat(message?.time.toString()),
                                lastSenderName = message?.senderName ?: "",
                            ),
                        )
                        observeRoomMessages(room.rid)
                    }
                }

                for (user in userNoRoom) {
                    chatUserItems.add(
                        ChatListState.ChatUserItemState(
                            isOnline = getActiveUserUseCase(user.uid),
                            id = user.uid,
                            name = user.username,
                            avatar = user.avatar,
                        ),
                    )
                }

                _state.update {
                    it.copy(
                        chatRoomItems = chatRoomItems,
                        chatUserItems = chatUserItems,
                        isLoading = false,
                        error = null,
                    )
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Failed to load rooms",
                    )
                }
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            logoutUserCase()
        }
    }

    private fun observeRoomMessages(roomId: String) {
        viewModelScope.launch {
            try {
                observeMessagesUseCase(roomId).collect { messages ->
                    val latestMessage = messages.lastOrNull()
                    if (latestMessage != null) {
                        Log.d(
                            "ChatListViewModel",
                            "New message received for room $roomId: ${latestMessage.content}",
                        )
                        _state.update { currentState ->
                            val updatedChatRooms = currentState.chatRoomItems.map { room ->
                                if (room.id == roomId) {
                                    room.copy(
                                        lastMessage = latestMessage.content,
                                        lastMessageTime = FormatTimeStamp.messageTimeFormat(latestMessage.time),
                                        lastSenderName = latestMessage.senderName,
                                    )
                                } else {
                                    room
                                }
                            }
                            currentState.copy(chatRoomItems = updatedChatRooms)
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("ChatListViewModel", "Error observing messages for room $roomId", e)
            }
        }
    }

    companion object {
        fun factory(context: Context) = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(ChatListViewModel::class.java)) {
                    val appContainer = AppContainer(context)

                    val getUserRoomForUserUseCase = GetUserRoomForUserUseCase(
                        userRoomRepository = appContainer.userRoomRepository,
                        authRepository = appContainer.authRepository,
                    )

                    val getUserRoomForRoomUseCase = GetUserRoomForRoomUseCase(
                        repository = appContainer.userRoomRepository,
                    )

                    val getRoomUseCase = GetRoomsUseCase(
                        repository = appContainer.roomRepository,
                    )

                    val getAllUsersInfoUseCase = GetAllUsersInfoUseCase(
                        repository = appContainer.userRepository,
                    )

                    val getActiveUserUseCase = GetActiveUserUseCase(
                        repository = appContainer.userRepository,
                    )

                    val observeMessagesUseCase = ObserveMessagesUseCase(
                        repository = appContainer.messageRepository,
                    )

                    val logoutUserCase = LogoutUserCase(
                        authRepository = appContainer.authRepository,
                    )

                    val currentUserId = appContainer.authRepository.getCurrentUserId()

                    return ChatListViewModel(
                        getUserRoomForUserUseCase = getUserRoomForUserUseCase,
                        getUserRoomForRoomUseCase = getUserRoomForRoomUseCase,
                        getRoomsUseCase = getRoomUseCase,
                        getAllUsersInfoUseCase = getAllUsersInfoUseCase,
                        getActiveUserUseCase = getActiveUserUseCase,
                        observeMessagesUseCase = observeMessagesUseCase,
                        logoutUserCase = logoutUserCase,
                        currentUserId = currentUserId,
                    ) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }
    }
}
