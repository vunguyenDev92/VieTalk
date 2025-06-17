package com.android.internship.presentation.components

import com.android.internship.data.model.Message
import com.android.internship.data.model.User
import java.time.LocalDateTime

sealed class MessageItem {
    data class TimeHeader(
        val timestamp: LocalDateTime,
    ) : MessageItem()

    data class MessageBubbles(
        val message: Message,
        val isFromMe: Boolean,
        val senderName: String?,
        val senderAvatarUrl: String?,
        val seenByUsers: List<User> = emptyList(),
        val isSeenByExpanded: Boolean = false,
        val avatarsOfUsersWhoLastSawThis: List<User> = emptyList(),
        val isCloseToHeader: Boolean = false,
    ) : MessageItem()

    data class TypingIndicator(
        val typingUsers: List<User>,
        val displayText: String,
    ) : MessageItem()
}
