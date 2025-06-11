package com.android.internship.presentation.screens.chatlist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.AccountCircle
import androidx.compose.material.icons.sharp.Menu
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.android.internship.presentation.components.chatlist.ChatListUserComponent
import com.android.internship.presentation.components.chatlist.ConnectWithOthersItem
import com.android.internship.presentation.components.chatlist.EmptyChatList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListChatsScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: ChatListViewModel,
    onChatClick: (String) -> Unit,
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { /* TODO: Handle menu click */ }) {
                        Icon(
                            imageVector = Icons.Sharp.Menu,
                            contentDescription = "Menu",
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
                    IconButton(onClick = { /* TODO: Handle profile click */ }) {
                        Icon(
                            imageVector = Icons.Sharp.AccountCircle,
                            contentDescription = "Profile",
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
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color.White),
        ) {
            ConnectWithOthersItem()

            when {
                state.isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        CircularProgressIndicator()
                    }
                }
                state.error != null -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(text = state.error ?: "An error occurred")
                    }
                }
                state.chatItems.isEmpty() -> {
                    EmptyChatList()
                }
                else -> {
                    ChatListUserComponent(
                        chatItems = state.chatItems,
                        onChatClick = onChatClick,
                    )
                }
            }
        }
    }
}
//
// @Preview(showBackground = true)
// @Composable
// fun PreviewListChatsScreen() {
//    val mockRepository = object : UserRepository {
//        override fun addUserRemote(uid: String, username: String, lastActiveTime: String, avatar: String?) {}
//        override suspend fun getUserRemote(uid: String): User? = null
//        override suspend fun getAllUserRemote(): List<User>? = null
//        override fun updateActiveTime(uid: String, lastActiveTime: String) {}
//        override fun updateAvatar(uid: String, avatar: String) {}
//        override suspend fun getUserLocal(uid: String): User? = null
//        override suspend fun getAllUserLocal(): List<User>? = null
//        override suspend fun saveLocalUser(user: User) {}
//        override suspend fun saveLocalUsers(users: List<User>) {}
//    }
//    val fakeChatEmptyItems = emptyList<ChatItemState>()
//    val fakeChatItems =
//    listOf(
//        ChatItemState(
//            id = "1",
//            name = "Preview User",
//            avatarUrl = "",
//            lastMessage = "Hello from preview!",
//            timestamp = "12:00",
//            isOnline = true,
//            isGroupChat = false,
//            memberAvatars = null,
//            lastSenderName = null,
//        ),
//        ChatItemState(
//            id = "2",
//            name = "Preview User",
//            avatarUrl = "",
//            lastMessage = "Hello from preview!",
//            timestamp = "12:00",
//            isOnline = true,
//            isGroupChat = false,
//            memberAvatars = null,
//            lastSenderName = null,
//        ),
//    )
//    val fakeState = ChatListState(chatItems = fakeChatItems)
//    val fakeViewModel = object : ChatListViewModel(GetAllUsersInfoUseCase(mockRepository)) {
//        override val state = MutableStateFlow(fakeState).asStateFlow()
//    }
//    ListChatsScreen(
//        onChatClick = { },
//        navController = rememberNavController(),
//        viewModel = fakeViewModel,
//    )
// }
