package com.android.internship.presentation.screens.chat

import com.android.internship.data.model.Room
import com.android.internship.data.model.User

data class MessageState(
    val room: Room? = null,
    val currentUserId: String = "",
    val userMap: Map<String, User> = emptyMap(),
    val messages: List<MessageItem> = emptyList(),
    val seenByExpandedState: Map<String, Boolean> = emptyMap(),
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val errorMessage: String? = null,
    val currentUser: User? = null,
    val expandedMessageId: String? = null,
    val topBarTitle: String = "",
    val topBarSubtitle: String = "",
    val topBarAvatarUrls: List<String> = emptyList(),
    val isPeerActive: Boolean = false,
    val isRefreshing: Boolean = false,
    val canLoadMore: Boolean = true,
    val isGroup: Boolean = false,
)
