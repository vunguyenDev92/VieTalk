package com.android.internship.presentation.screens.chatlist

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.android.internship.di.AppContainer
import com.android.internship.domain.usecase.GetActiveUserUseCase
import com.android.internship.domain.usecase.GetAllUsersInfoUseCase
import com.android.internship.domain.usecase.GetMessagesUseCase
import com.android.internship.domain.usecase.GetRoomUseCase
import com.android.internship.domain.usecase.GetUserRoomForRoomUseCase
import com.android.internship.domain.usecase.GetUserRoomForUserUseCase
import com.android.internship.domain.usecase.LogoutUserCase
import com.android.internship.presentation.utils.FormatTimeStamp
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ChatListViewModel(
    private val getUserRoomForUserUseCase: GetUserRoomForUserUseCase,
    private val getUserRoomForRoomUseCase: GetUserRoomForRoomUseCase,
    private val getRoomUseCase: GetRoomUseCase,
    private val getAllUsersInfoUseCase: GetAllUsersInfoUseCase,
    private val getMessagesUseCase: GetMessagesUseCase,
    private val getActiveUserUseCase: GetActiveUserUseCase,
    private val logoutUserCase: LogoutUserCase,
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
                val users = getAllUsersInfoUseCase()
                val userNoRoom = users.toMutableList()

                userNoRoom.removeIf { it.uid == currentUserId }

                for (userRoom in userRooms) {
                    val room = getRoomUseCase(userRoom.rid)
                    val messages = getMessagesUseCase(userRoom.rid, limit = 1)
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

                        val timestamp = (messages?.lastOrNull()?.time ?: "0").toLong()

                        val lastMessageTime = FormatTimeStamp.messageTimeFormat(timestamp)

                        chatRoomItems.add(
                            ChatListState.ChatRoomItemState(
                                isGroupChat = room.isGroup,
                                isActive = isActive,
                                id = room.rid,
                                name = name,
                                memberAvatars = memberAvatar,
                                lastMessage = messages?.lastOrNull()?.content ?: "",
                                lastMessageTime = lastMessageTime,
                                lastSenderName = users.find { it.uid == messages?.lastOrNull()?.uid }?.username,
                            ),
                        )
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

                    val getRoomUseCase = GetRoomUseCase(
                        repository = appContainer.roomRepository,
                    )

                    val getAllUsersInfoUseCase = GetAllUsersInfoUseCase(
                        repository = appContainer.userRepository,
                    )

                    val getMessagesUseCase = GetMessagesUseCase(
                        repository = appContainer.messageRepository,
                    )

                    val getActiveUserUseCase = GetActiveUserUseCase(
                        repository = appContainer.userRepository,
                    )

                    val logoutUserCase = LogoutUserCase(
                        authRepository = appContainer.authRepository,
                    )

                    val currentUserId = appContainer.authRepository.getCurrentUserId()

                    return ChatListViewModel(
                        getUserRoomForUserUseCase = getUserRoomForUserUseCase,
                        getUserRoomForRoomUseCase = getUserRoomForRoomUseCase,
                        getRoomUseCase = getRoomUseCase,
                        getAllUsersInfoUseCase = getAllUsersInfoUseCase,
                        getMessagesUseCase = getMessagesUseCase,
                        getActiveUserUseCase = getActiveUserUseCase,
                        logoutUserCase = logoutUserCase,
                        currentUserId = currentUserId,
                    ) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }
    }
}
