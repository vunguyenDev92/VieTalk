package com.android.internship.presentation.components.chatlist

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.android.internship.presentation.screens.chatlist.ChatListState

val chats = emptyList<ChatListState>()

@Composable
fun ChatListUserComponent(onChatClick: (String) -> Unit) {
    LazyColumn(
        contentPadding = PaddingValues(vertical = 8.dp),
    ) {
        items(chats) { chat ->
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
