package com.android.internship.presentation.components.chatlist

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.android.internship.presentation.screens.chatlist.ChatItemState

// Sample data for preview
private val sampleChats = listOf(
    ChatItemState(
        id = "1",
        name = "Albert Flores",
        avatarUrl = "https://randomuser.me/api/portraits/men/1.jpg",
        lastMessage = "Hello, how are you today?",
        timestamp = "03:50 PM",
        isOnline = true,
        isGroupChat = false,
        memberAvatars = null,
        lastSenderName = null
    ),
    ChatItemState(
        id = "2",
        name = "Group Gaming",
        avatarUrl = "",
        lastMessage = "Let's play tonight!",
        timestamp = "03:45 PM",
        isOnline = false,
        isGroupChat = true,
        memberAvatars = listOf(
            "https://randomuser.me/api/portraits/men/2.jpg",
            "https://randomuser.me/api/portraits/women/3.jpg",
            "https://randomuser.me/api/portraits/men/4.jpg"
        ),
        lastSenderName = "John"
    ),
    ChatItemState(
        id = "3",
        name = "Sara Johnson",
        avatarUrl = "https://randomuser.me/api/portraits/women/5.jpg",
        lastMessage = "See you tomorrow!",
        timestamp = "03:30 PM",
        isOnline = true,
        isGroupChat = false,
        memberAvatars = null,
        lastSenderName = null
    ),
    ChatItemState(
        id = "4",
        name = "Team Project",
        avatarUrl = "",
        lastMessage = "Meeting at 5 PM",
        timestamp = "03:20 PM",
        isOnline = false,
        isGroupChat = true,
        memberAvatars = listOf(
            "https://randomuser.me/api/portraits/men/6.jpg",
            "https://randomuser.me/api/portraits/women/7.jpg"
        ),
        lastSenderName = "Alice"
    )
)

@Composable
fun ChatListUserComponent(
    chatItems: List<ChatItemState> = sampleChats, // Use sample data for preview
    onChatClick: (String) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(vertical = 8.dp),
    ) {
        items(chatItems) { chat ->
            if (chat.isGroupChat) {
                ChatIsGroupItem(
                    name = chat.name,
                    memberAvatars = chat.memberAvatars ?: emptyList(),
                    lastMessage = chat.lastMessage,
                    lastSenderName = chat.lastSenderName ?: "",
                    timestamp = chat.timestamp,
                    onClick = { onChatClick(chat.id) },
                )
            } else {
                ChatIsNotGroupItem(
                    avatarUrl = chat.avatarUrl,
                    name = chat.name,
                    lastMessage = chat.lastMessage,
                    timestamp = chat.timestamp,
                    isOnline = chat.isOnline,
                    onClick = { onChatClick(chat.id) },
                )
            }
        }
    }
}
