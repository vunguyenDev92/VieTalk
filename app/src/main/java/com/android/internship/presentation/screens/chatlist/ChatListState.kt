package com.android.internship.presentation.screens.chatlist

data class ChatListState(
    val isOnline: Boolean,
    val isGroupChat: Boolean = false,
    val memberAvatars: List<String>? = null,
    val id: String,
    val name: String,
    val avatarUrl: String,
    val lastMessage: String,
    val timestamp: String,
    val lastSenderName: String? = null,
)
