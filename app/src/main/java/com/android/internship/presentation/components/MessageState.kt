package com.android.internship.presentation.components

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
)
