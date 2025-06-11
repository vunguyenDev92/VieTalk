// file: com/android/internship/presentation/components/utils/MessageProcessor.kt
package com.android.internship.presentation.components.utils

import com.android.internship.data.model.Message
import com.android.internship.data.model.Room
import com.android.internship.data.model.User
import com.android.internship.data.model.UserRoom
import com.android.internship.presentation.components.MessageItem
import java.time.Duration
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

fun processMessagesToItems(
    messages: List<Message>,
    room: Room,
    usersInRoom: List<User>,
    userRoomDetails: List<UserRoom>,
    currentUserId: String,
    expandedMessageId: String?,
): List<MessageItem> {
    val userMap = usersInRoom.associateBy { it.uid }
    val shouldShowSenderName = room.isGroup && usersInRoom.size > 2
    val items = mutableListOf<MessageItem>()
    var lastMessageTimestamp: LocalDateTime? = null

    val sortedMessages = messages.sortedBy { it.time }
    val allMessagesMap = messages.associateBy { it.mid }

    sortedMessages.forEachIndexed { index, message ->
        val messageTime = try {
            val instant = Instant.ofEpochMilli(message.time.toLong())
            LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
        } catch (e: Exception) {
            LocalDateTime.now()
        }

        val shouldShowTimeHeader = if (lastMessageTimestamp == null) {
            true
        } else {
            Duration.between(lastMessageTimestamp, messageTime).toHours() >= 1
        }

        if (shouldShowTimeHeader) {
            items.add(MessageItem.TimeHeader(messageTime))
        }
        lastMessageTimestamp = messageTime

        val isFromMe = message.uid == currentUserId
        val senderInfo = userMap[message.uid]
        val senderName = if (shouldShowSenderName && !isFromMe) {
            senderInfo?.username ?: "Unknown User"
        } else {
            null
        }

        val nextMessage = sortedMessages.getOrNull(index + 1)
        val showAvatar = nextMessage == null || nextMessage.uid != message.uid

        val currentMessageTimestamp = message.time.toLongOrNull() ?: 0L

        val seenByUsers = userRoomDetails.mapNotNull { userDetail ->
            if (userDetail.uid == message.uid) return@mapNotNull null

            val lastSeenMessageId = userDetail.lastSeenMessages ?: return@mapNotNull null
            val lastSeenMessageObject = allMessagesMap[lastSeenMessageId] ?: return@mapNotNull null
            val lastSeenTimestamp = lastSeenMessageObject.time.toLongOrNull() ?: 0L

            if (lastSeenTimestamp >= currentMessageTimestamp) {
                userMap[userDetail.uid]
            } else {
                null
            }
        }

        val isSeenByExpanded = message.mid == expandedMessageId

        items.add(
            MessageItem.MessageBubbles(
                message = message,
                isFromMe = isFromMe,
                senderName = senderName,
                senderAvatarUrl = senderInfo?.avatar,
                seenByUsers = seenByUsers,
                isSeenByExpanded = isSeenByExpanded,
// 				showAvatar = showAvatar
            ),
        )
    }

    val typingUsers = mutableListOf<User>()
    userRoomDetails.forEach { detail ->
        val typingTime = detail.typingTime?.toLongOrNull() ?: 0L
        val isRecent = (System.currentTimeMillis() - typingTime) < 5000

        if (detail.uid != currentUserId && isRecent) {
            userMap[detail.uid]?.let { typingUsers.add(it) }
        }
    }

    if (typingUsers.isNotEmpty()) {
        val displayText = when (val count = typingUsers.size) {
            1 -> "${typingUsers[0].username} is typing..."
            2 -> "${typingUsers[0].username} and ${typingUsers[1].username} are typing..."
            else -> "${typingUsers[0].username}, ${typingUsers[1].username} and ${count - 2} others are typing..."
        }

        items.add(
            MessageItem.TypingIndicator(
                typingUsers = typingUsers,
                displayText = displayText,
            ),
        )
    }

    return items.reversed()
}
