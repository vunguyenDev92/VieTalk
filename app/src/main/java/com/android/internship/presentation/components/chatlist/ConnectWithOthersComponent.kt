package com.android.internship.presentation.components.chatlist

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

val chatsNonData = emptyList<ChatNonData>()

@Composable
fun ConnectWithOthersItem() {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 8.dp),
    ) {
        items(chatsNonData) { chat ->
            ChatNonMessageItem(
                avatarUrl = chat.avatarUrl,
            )
        }
    }
    HorizontalDivider(thickness = 2.dp)
}

data class ChatNonData(
    val avatarUrl: String,
)
