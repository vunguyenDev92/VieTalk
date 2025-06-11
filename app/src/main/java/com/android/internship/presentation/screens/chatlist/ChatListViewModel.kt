package com.android.internship.presentation.screens.chatlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.internship.domain.repository.RoomRepository
import com.android.internship.domain.repository.UserRepository
import com.android.internship.domain.repository.UserRoomRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ChatListViewModel(
    private val roomRepository: RoomRepository,
    private val userRoomRepository: UserRoomRepository,
    private val userRepository: UserRepository,
    private val currentUserId: String,
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
                val userRooms = userRoomRepository.getUserRoomRemote(currentUserId)

                val chatItems = userRooms.mapNotNull { userRoom ->
                    val room = roomRepository.getRoomRemote(userRoom.rid)
                    room?.let {
                        if (it.isGroup) {
                            val groupMembers = userRoomRepository.getUserRoomRemote(it.rid)
                            val memberAvatars = groupMembers.mapNotNull { member ->
                                userRepository.getUserRemote(member.uid)?.avatar
                            }

                            ChatItemState(
                                id = it.rid,
                                name = it.name ?: "Group Chat",
                                avatarUrl = it.avatar ?: "",
                                lastMessage = "",
                                timestamp = userRoom.lastSeenMessages ?: "",
                                isOnline = false,
                                isGroupChat = true,
                                memberAvatars = memberAvatars,
                                lastSenderName = null,
                            )
                        } else {
                            val otherUserRoom = userRoomRepository.getUserRoomRemote(it.rid)
                                .firstOrNull { member -> member.uid != currentUserId }

                            val otherUser = otherUserRoom?.let { member ->
                                userRepository.getUserRemote(member.uid)
                            }

                            ChatItemState(
                                id = it.rid,
                                name = otherUser?.username ?: "Unknown User",
                                avatarUrl = otherUser?.avatar ?: "",
                                lastMessage = "",
                                timestamp = userRoom.lastSeenMessages ?: "",
                                isOnline = otherUser?.lastActiveTime?.isNotEmpty() == true,
                                isGroupChat = false,
                                memberAvatars = null,
                                lastSenderName = null,
                            )
                        }
                    }
                }

                _state.update {
                    it.copy(
                        chatItems = chatItems,
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
}
