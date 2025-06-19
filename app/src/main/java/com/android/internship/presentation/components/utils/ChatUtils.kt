package com.android.internship.presentation.components.utils

import com.android.internship.data.model.Message
import com.android.internship.data.model.Room
import com.android.internship.data.model.User
import com.android.internship.data.model.UserRoom
import com.android.internship.presentation.screens.chat.MessageItem
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
    val isTrueGroup = room.isGroup && usersInRoom.size > 2
    val items = mutableListOf<MessageItem>()
    var lastMessageTimestamp: LocalDateTime? = null

    val sortedMessages = messages.sortedBy { it.time }
    val allMessagesMap = messages.associateBy { it.mid }

    val messagesWithTimeHeaderAbove = mutableSetOf<String>()

    val lastSeenMessageIdToUsersMap = mutableMapOf<String, MutableList<User>>()
    userRoomDetails.forEach { userDetail ->
        if (userDetail.uid != currentUserId) {
            userDetail.lastSeenMessages?.let { lastSeenMid ->
                userMap[userDetail.uid]?.let { user ->
                    lastSeenMessageIdToUsersMap.getOrPut(lastSeenMid) { mutableListOf() }.add(user)
                }
            }
        }
    }

    sortedMessages.forEachIndexed { index, message ->
        val messageTime = try {
            LocalDateTime.ofInstant(Instant.ofEpochMilli(message.time.toLong()), ZoneId.systemDefault())
        } catch (e: Exception) {
            LocalDateTime.now()
        }

        val shouldShowTimeHeader = lastMessageTimestamp == null ||
            Duration.between(lastMessageTimestamp, messageTime).toHours() >= 1

        if (shouldShowTimeHeader) {
            items.add(MessageItem.TimeHeader(messageTime))
            messagesWithTimeHeaderAbove.add(message.mid)
        }
        lastMessageTimestamp = messageTime

        val isFromMe = message.uid == currentUserId
        val senderInfo = userMap[message.uid]
        val nextMessage = sortedMessages.getOrNull(index + 1)
        val previousMessage = sortedMessages.getOrNull(index - 1)

        val showAvatar = shouldShowAvatar(message, nextMessage)
        val showSenderName = shouldShowSenderName(message, previousMessage, isFromMe, isTrueGroup)
        val senderName = if (showSenderName) senderInfo?.username ?: "Unknown User" else null

        val currentMessageTimestamp = message.time.toLongOrNull() ?: 0L

        val seenByUsers = userRoomDetails.mapNotNull { userDetail ->
            if (userDetail.uid == currentUserId) {
                return@mapNotNull null
            }
            val lastSeenMessageId = userDetail.lastSeenMessages ?: return@mapNotNull null
            val lastSeenMessageObject = allMessagesMap[lastSeenMessageId] ?: return@mapNotNull null
            val lastSeenTimestamp = lastSeenMessageObject.time.toLongOrNull() ?: 0L
            if (lastSeenTimestamp >= currentMessageTimestamp) userMap[userDetail.uid] else null
        }

        val avatarsToShow = lastSeenMessageIdToUsersMap[message.mid] ?: emptyList()

        val isCloseToHeader = messagesWithTimeHeaderAbove.contains(message.mid)
        val isSeenByExpanded = (message.mid == expandedMessageId) && !isCloseToHeader

        items.add(
            MessageItem.MessageBubbles(
                message = message,
                isFromMe = isFromMe,
                senderName = senderName,
                senderAvatarUrl = senderInfo?.avatar,
                seenByUsers = seenByUsers,
                isSeenByExpanded = isSeenByExpanded,
                avatarsOfUsersWhoLastSawThis = avatarsToShow,
                isCloseToHeader = isCloseToHeader,
                showAvatar = showAvatar,
            ),
        )
    }

    val typingUsers = userRoomDetails.mapNotNull { detail ->
        val typingTime = detail.typingTime?.toLongOrNull() ?: 0L
        val isRecent = (System.currentTimeMillis() - typingTime) < 5000
        if (detail.uid != currentUserId && isRecent) userMap[detail.uid] else null
    }

    if (typingUsers.isNotEmpty()) {
        val displayText = when (val count = typingUsers.size) {
            1 -> "${typingUsers.first().username} is typing..."
            2 -> "${typingUsers[0].username} and ${typingUsers[1].username} are typing..."
            else -> "${typingUsers[0].username}, ${typingUsers[1].username} and ${count - 2} others are typing..."
        }
        items.add(MessageItem.TypingIndicator(typingUsers, displayText))
    }

    return items.reversed()
}

private fun shouldShowAvatar(
    currentMessage: Message,
    nextMessage: Message?,
): Boolean {
    if (nextMessage == null || nextMessage.uid != currentMessage.uid) return true
    val currentTime = currentMessage.time.toLongOrNull() ?: 0L
    val nextTime = nextMessage.time.toLongOrNull() ?: 0L
    return (nextTime - currentTime) / (1000 * 60) > 10
}

private fun shouldShowSenderName(
    currentMessage: Message,
    previousMessage: Message?,
    isFromMe: Boolean,
    isTrueGroup: Boolean,
): Boolean {
    if (isFromMe || !isTrueGroup) return false
    if (previousMessage == null || previousMessage.uid != currentMessage.uid) return true
    val currentTime = currentMessage.time.toLongOrNull() ?: 0L
    val previousTime = previousMessage.time.toLongOrNull() ?: 0L
    return (currentTime - previousTime) / (1000 * 60) > 10
}
