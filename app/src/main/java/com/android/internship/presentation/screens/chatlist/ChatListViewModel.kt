package com.android.internship.presentation.screens.chatlist

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.internship.domain.repository.MessageRepository
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
    private val messageRepository: MessageRepository,
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
                Log.d("ChatListViewModel", "Found ${userRooms.size} rooms for user $currentUserId")

                val chatItems = mutableListOf<ChatItemState>()
                val processedUserPairs = mutableSetOf<String>()

                for (userRoom in userRooms) {
                    try {
                        val lastMessage = messageRepository.getRemoteMessages(userRoom.rid, startMessageId = null, limit = 1)?.firstOrNull()?.content
                        val time = messageRepository.getRemoteMessages(userRoom.rid, startMessageId = null, limit = 1)?.firstOrNull()?.time
                        val room = roomRepository.getRoomRemote(userRoom.rid)
                        Log.d("ChatListViewModel", "Room ${userRoom.rid} details: $room")

                        if (room != null) {
                            if (room.isGroup) {
                                val groupMembers = userRoomRepository.getUserRoomsForRoom(room.rid)
                                val memberAvatars = groupMembers.mapNotNull { member ->
                                    userRepository.getUserRemote(member.uid)?.avatar
                                }

                                chatItems.add(
                                    ChatItemState(
                                        id = room.rid,
                                        name = room.name ?: "Group Chat",
                                        avatarUrl = room.avatar ?: "",
                                        lastMessage = lastMessage.toString(),
                                        timestamp = time ?: "",
                                        isOnline = false,
                                        isGroupChat = true,
                                        memberAvatars = memberAvatars,
                                        lastSenderName = null,
                                    ),
                                )
                            } else {
                                val otherUserRoom = userRoomRepository.getUserRoomsForRoom(room.rid)
                                    .firstOrNull { member -> member.uid != currentUserId }

                                Log.d("ChatListViewModel", "Other user room for room ${room.rid}: $otherUserRoom")

                                if (otherUserRoom != null) {
                                    val otherUser = userRepository.getUserRemote(otherUserRoom.uid)
                                    Log.d("ChatListViewModel", "Other user details: $otherUser")

                                    val userPairKey = if (currentUserId < otherUserRoom.uid) {
                                        "${currentUserId}_${otherUserRoom.uid}"
                                    } else {
                                        "${otherUserRoom.uid}_$currentUserId"
                                    }

                                    if (!processedUserPairs.contains(userPairKey)) {
                                        processedUserPairs.add(userPairKey)
                                        chatItems.add(
                                            ChatItemState(
                                                id = room.rid,
                                                name = otherUser?.username ?: "Unknown User",
                                                avatarUrl = otherUser?.avatar ?: "",
                                                lastMessage = lastMessage.toString(),
                                                timestamp = time ?: "",
                                                isOnline = otherUser?.lastActiveTime?.isNotEmpty() == true,
                                                isGroupChat = false,
                                                memberAvatars = null,
                                                lastSenderName = null,
                                            ),
                                        )
                                    }
                                }
                            }
                        }
                    } catch (e: Exception) {
                        Log.e("ChatListViewModel", "Error processing room ${userRoom.rid}: ${e.message}")
                    }
                }

                Log.d("ChatListViewModel", "Final chat items: $chatItems")

                _state.update {
                    it.copy(
                        chatItems = chatItems,
                        isLoading = false,
                        error = null,
                    )
                }
            } catch (e: Exception) {
                Log.e("ChatListViewModel", "Error loading rooms: ${e.message}")
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
