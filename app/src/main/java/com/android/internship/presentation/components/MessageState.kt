package com.android.internship.presentation.components

import com.android.internship.data.model.Room
import com.android.internship.data.model.User
data class MessageState(
    val currentUserId: String = "",
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val canLoadMore: Boolean = true,
    val isRefreshing: Boolean = false,
    val isNetworkAvailable: Boolean = true,
    val room: Room? = null,
    val messages: List<MessageItem> = emptyList(),
    val userMap: Map<String, User> = emptyMap(),
    val currentUser: User? = null,
    val topBarTitle: String = "",
    val topBarSubtitle: String = "",
    val topBarAvatarUrls: List<String> = emptyList(),
    val isPeerActive: Boolean = false,
    val expandedMessageId: String? = null,
    val errorMessage: String? = null,
)
