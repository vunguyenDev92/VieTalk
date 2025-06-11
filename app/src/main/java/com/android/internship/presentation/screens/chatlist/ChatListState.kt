package com.android.internship.presentation.screens.chatlist

data class ChatItemState(
    val isOnline: Boolean = false,
    val isGroupChat: Boolean = false,
    val memberAvatars: List<String>? = null,
    val id: String = "",
    val name: String = "",
    val avatarUrl: String = "",
    val lastMessage: String = "",
    val timestamp: String = "",
    val lastSenderName: String? = null,
)

data class ChatListState(
    val chatItems: List<ChatItemState> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)
