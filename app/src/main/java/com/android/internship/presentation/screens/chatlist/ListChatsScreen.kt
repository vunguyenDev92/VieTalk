package com.android.internship.presentation.screens.listchats

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.AccountCircle
import androidx.compose.material.icons.sharp.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.android.internship.presentation.components.chatlist.ChatListUserComponent
import com.android.internship.presentation.components.chatlist.ConnectWithOthersItem
import com.android.internship.presentation.components.chatlist.EmptyChatList
import com.android.internship.presentation.components.chatlist.chats

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListChatsScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    onChatClick: (String) -> Unit,
) {
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
                    containerColor = Color.White,
                ),
            )
        },
    ) { innerPadding ->
        if (chats.isEmpty()) {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(Color.White),
            ) {
                ConnectWithOthersItem()

                EmptyChatList()
            }
        } else {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(Color.White),
            ) {
                ConnectWithOthersItem()

                ChatListUserComponent(onChatClick)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewListChatsScreen() {
    ListChatsScreen(
        onChatClick = { },
        navController = rememberNavController(),
    )
}
