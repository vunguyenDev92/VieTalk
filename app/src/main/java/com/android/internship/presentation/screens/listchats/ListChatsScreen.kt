package com.android.internship.presentation.screens.listchats

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.AccountCircle
import androidx.compose.material.icons.sharp.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.android.internship.presentation.components.ChatIsGroupItem
import com.android.internship.presentation.components.ChatIsNotGroupItem
import com.android.internship.presentation.components.ChatNonMessageItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListChatsScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    onChatClick: (String) -> Unit,
) {
    val chats = emptyList<ChatData>()
    val chatsNonData = emptyList<ChatNonData>()

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = Icons.Sharp.Menu,
                            contentDescription = "Menu description",
                            Modifier.size(30.dp),
                        )
                    }
                },
                title = {
                    Text(
                        text = "Chats",
                        style = TextStyle(
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Bold,
                        ),
                    )
                },
                actions = {
                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = Icons.Sharp.AccountCircle,
                            contentDescription = "Edit Profile",
                            Modifier.size(40.dp),
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                ),
            )
        },
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 8.dp),
            ) {
                items(chatsNonData) { chat ->
                    ChatNonMessageItem(
                        avatarUrl = chat.avatarUrl,
                        memberAvatars = chat.memberAvatars ?: emptyList(),
                    )
                }
            }
            HorizontalDivider(thickness = 2.dp)

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
    }
}

data class ChatData(
    val id: String,
    val name: String,
    val avatarUrl: String,
    val lastMessage: String,
    val timestamp: String,
    val isOnline: Boolean,
    val isGroupChat: Boolean = false,
    val memberAvatars: List<String>? = null,
    val lastSenderName: String? = null,
)

data class ChatNonData(
    val avatarUrl: String,
    val memberAvatars: List<String>? = null,
)

@Preview(showBackground = true)
@Composable
fun PreviewListChatsScreen() {
    ListChatsScreen(
        onChatClick = { },
        navController = rememberNavController(),
    )
}
