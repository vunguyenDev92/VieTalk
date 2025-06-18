package com.android.internship.presentation.screens.chatlist

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.android.internship.data.model.Room
import com.android.internship.data.model.User
import com.android.internship.data.model.UserRoom
import com.android.internship.di.AppContainer
import com.android.internship.domain.usecase.CreateRoomUseCase
import com.android.internship.domain.usecase.GetAllUsersInfoUseCase
import com.android.internship.domain.usecase.GetCurrentUserProfileUseCase
import com.android.internship.domain.usecase.LogoutUserCase
import com.android.internship.domain.usecase.ObserveRoomUseCase
import com.android.internship.domain.usecase.ObserveUserRoomForUserUseCase
import com.android.internship.presentation.utils.FormatTimeStamp
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ChatListViewModel(
    private val observeUserRoomForUserUseCase: ObserveUserRoomForUserUseCase,
    private val getAllUsersInfoUseCase: GetAllUsersInfoUseCase,
    private val logoutUserCase: LogoutUserCase,
    private val observeRoomUseCase: ObserveRoomUseCase,
    private val createRoomUseCase: CreateRoomUseCase,
    private val currentUserId: String?,
    private val getCurrentUserProfileUseCase: GetCurrentUserProfileUseCase,
) : ViewModel() {
    private val _state = MutableStateFlow(ChatListState())
    val state = _state.asStateFlow()

    init {
        loadUserRooms()
        loadCurrentUserProfile()
    }

    private fun loadCurrentUserProfile() {
        viewModelScope.launch {
            try {
                val userId = currentUserId
                val user = getCurrentUserProfileUseCase(userId.toString())
                _state.update { it.copy(currentUser = user) }
            } catch (e: Exception) {
            }
        }
    }

    private fun loadUserRooms() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            try {
                val users = getAllUsersInfoUseCase()
                observeUserRoomForUserUseCase()?.let {
                    observeRoomUseCase().combine(it) { rooms, userRooms ->
                        _state.update {
                            it.copy(
                                chatRoomItems = generateChatRoomStates(userRooms, rooms, users),
                                chatUserItems = generateChatUserStates(userRooms, rooms, users),
                                isLoading = false,
                                error = null,
                            )
                        }
                    }.collectLatest {}
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

    private fun generateChatRoomStates(
        userRoomsForUser: List<UserRoom>,
        rooms: List<Room>,
        users: List<User>,
    ): List<ChatListState.ChatRoomItemState> {
        val chatRoomItems = mutableListOf<ChatListState.ChatRoomItemState>()

        for (userRoom in userRoomsForUser) {
            rooms.find { it.rid == userRoom.rid }?.let { room ->
                var name = room.name.toString()
                var isActive = false
                val message = room.lastMessage
                val memberAvatar = room.listUser.filter { it != currentUserId }.mapNotNull { uid ->
                    users.find { it.uid == uid }?.avatar
                }

                if (room.isGroup == false) {
                    name = users.find { it.uid == room.listUser.firstOrNull { it != currentUserId } }?.username.toString()
                    users.find { it.uid == room.listUser.firstOrNull { it != currentUserId } }?.lastActiveTime?.let {
                        isActive = if (it.isEmpty()) {
                            false
                        } else {
                            System.currentTimeMillis() - it.toLong() <= 5 * 60 * 1000
                        }
                    }
                }

                chatRoomItems.add(
                    ChatListState.ChatRoomItemState(
                        isGroupChat = room.isGroup,
                        isActive = isActive,
                        id = room.rid,
                        name = name,
                        memberAvatars = memberAvatar,
                        lastMessage = message.content,
                        lastMessageTime = FormatTimeStamp.messageTimeFormat(message.time),
                        lastSenderName = message.senderName,
                        updateAt = room.updatedAt,
                    ),
                )
            }
        }

        chatRoomItems.sortByDescending { it.updateAt }

        return chatRoomItems
    }

    private fun generateChatUserStates(
        userRoomsForUser: List<UserRoom>,
        rooms: List<Room>,
        users: List<User>,
    ): List<ChatListState.ChatUserItemState> {
        val chatUserItems = mutableListOf<ChatListState.ChatUserItemState>()

        for (user in users) {
            if (rooms.find {
                    it.isGroup == false &&
                        it.rid in userRoomsForUser.map { it.rid } &&
                        it.listUser.contains(user.uid)
                } == null
            ) {
                val isActive = if (user.lastActiveTime.isEmpty()) {
                    false
                } else {
                    System.currentTimeMillis() - user.lastActiveTime.toLong() <= 5 * 60 * 1000
                }

                chatUserItems.add(
                    ChatListState.ChatUserItemState(
                        isOnline = isActive,
                        id = user.uid,
                        name = user.username,
                        avatar = user.avatar,
                    ),
                )
            }
        }

        return chatUserItems
    }

    fun createGroup(uid: String): String? {
        try {
            currentUserId?.let {
                return createRoomUseCase(userIds = listOf(uid, it))
            }
            return null
        } catch (_: IllegalArgumentException) {
            return null
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

                    val observeUserRoomForUserUseCase = ObserveUserRoomForUserUseCase(
                        userRoomRepository = appContainer.userRoomRepository,
                        authRepository = appContainer.authRepository,
                    )

                    val getCurrentUserProfileUseCase = GetCurrentUserProfileUseCase(
                        userRepository = appContainer.userRepository,
                    )
                    val getAllUsersInfoUseCase = GetAllUsersInfoUseCase(
                        repository = appContainer.userRepository,
                    )

                    val observeRoomUseCase = ObserveRoomUseCase(
                        repository = appContainer.roomRepository,
                    )

                    val logoutUserCase = LogoutUserCase(
                        authRepository = appContainer.authRepository,
                    )

                    val createRoomUseCase = CreateRoomUseCase(
                        roomRepository = appContainer.roomRepository,
                        userRoomRepository = appContainer.userRoomRepository,
                    )

                    val currentUserId = appContainer.authRepository.getCurrentUserId()

                    return ChatListViewModel(
                        observeUserRoomForUserUseCase = observeUserRoomForUserUseCase,
                        getAllUsersInfoUseCase = getAllUsersInfoUseCase,
                        observeRoomUseCase = observeRoomUseCase,
                        logoutUserCase = logoutUserCase,
                        createRoomUseCase = createRoomUseCase,
                        currentUserId = currentUserId,
                        getCurrentUserProfileUseCase = getCurrentUserProfileUseCase,
                    ) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }
    }
}
