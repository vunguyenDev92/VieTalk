package com.android.internship.presentation.screens.chatlist

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.android.internship.R
import com.android.internship.presentation.components.CommonDialog
import com.android.internship.presentation.components.CommonNavigationDrawer
import com.android.internship.presentation.components.CommonNavigationDrawerItem
import com.android.internship.presentation.components.CommonProgressIndicator
import com.android.internship.presentation.components.TextButtonDialog
import com.android.internship.presentation.navigation.Screen
import com.android.internship.presentation.theme.Black
import com.android.internship.presentation.theme.Red
import com.android.internship.presentation.theme.White
import com.android.internship.presentation.utils.NetworkMonitor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatListScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: ChatListViewModel = viewModel(factory = ChatListViewModel.factory(navController.context)),
    networkMonitor: NetworkMonitor = NetworkMonitor(navController.context),
) {
    val chatListScreenState by viewModel.state.collectAsState()
    var showLogoutDialog by remember { mutableStateOf(false) }
    var showDrawer by remember { mutableStateOf(false) }
    val isConnected by networkMonitor.isConnected.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White),
    ) {
        TopAppBar(
            windowInsets = WindowInsets(top = 0.dp),
            colors = TopAppBarColors(
                containerColor = White,
                scrolledContainerColor = White,
                navigationIconContentColor = Black,
                titleContentColor = Black,
                actionIconContentColor = Black,
            ),
            title = {
                Text(
                    stringResource(R.string.chats),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold,
                    ),
                )
            },
            navigationIcon = {
                Icon(
                    imageVector = Icons.Filled.Menu,
                    contentDescription = stringResource(R.string.open_drawer),
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .size(40.dp)
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() },
                            onClick = { showDrawer = true },
                        ),
                )
            },
            actions = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_group),
                    contentDescription = stringResource(R.string.create_new_group),
                    modifier = Modifier
                        .padding(end = 16.dp)
                        .size(40.dp)
                        .clip(RoundedCornerShape(1000.dp))
                        .background(Color(0x66bbbbbb))
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() },
                            onClick = {
                                navController.navigate(Screen.GroupEditor)
                            },
                        ),
                )
            },
        )

        if (!isConnected) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = stringResource(R.string.no_internet_connection),
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = Red,
                    ),
                )
            }
        } else if (chatListScreenState.error != null) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = chatListScreenState.error ?: stringResource(R.string.an_error_occurred),
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = Red,
                    ),
                )
            }
        }

        if (chatListScreenState.chatUserItems.isNotEmpty()) {
            ConnectWithOthersItem(
                chatItems = chatListScreenState.chatUserItems,
                onClick = {
                    viewModel.createGroup(it)?.let {
                        navController.navigate(route = Screen.Chat(it)) {
                            launchSingleTop = true
                        }
                    }
                },
            )
        }

        when {
            chatListScreenState.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    CommonProgressIndicator()
                }
            }

            chatListScreenState.chatRoomItems.isEmpty() -> {
                EmptyChatList()
            }

            else -> {
                LazyColumn(
                    contentPadding = PaddingValues(vertical = 8.dp),
                ) {
                    items(chatListScreenState.chatRoomItems.size) { index ->
                        ChatRoomItem(
                            isGroup = chatListScreenState.chatRoomItems[index].isGroupChat,
                            isOnline = chatListScreenState.chatRoomItems[index].isActive,
                            name = chatListScreenState.chatRoomItems[index].name,
                            memberAvatars = chatListScreenState.chatRoomItems[index].memberAvatars ?: emptyList(),
                            lastMessage = chatListScreenState.chatRoomItems[index].lastMessage,
                            lastSenderName = chatListScreenState.chatRoomItems[index].lastSenderName,
                            lastMessageTime = chatListScreenState.chatRoomItems[index].lastMessageTime,
                            onClick = {
                                navController.navigate(route = Screen.Chat(chatListScreenState.chatRoomItems[index].id)) {
                                    launchSingleTop = true
                                }
                            },
                        )
                    }
                }
            }
        }
    }

    CommonNavigationDrawer(
        isDrawerOpen = showDrawer,
        closeDrawer = { showDrawer = false },
        content = {
            CommonNavigationDrawerItem(
                label = stringResource(R.string.log_out),
                icon = R.drawable.ic_logout,
                onClick = {
                    showDrawer = false
                    showLogoutDialog = true
                },
            )
        },
    )

    if (showLogoutDialog) {
        CommonDialog(
            title = stringResource(R.string.log_out),
            content = stringResource(R.string.are_you_sure_you_want_to_log_out),
            onDismissRequest = {
                showLogoutDialog = false
            },
            button = {
                TextButtonDialog(
                    text = stringResource(R.string.cancel).uppercase(),
                    onClick = {
                        showLogoutDialog = false
                    },
                )
                TextButtonDialog(
                    text = stringResource(R.string.log_out).uppercase(),
                    color = Red,
                    onClick = {
                        viewModel.logout()
                        showLogoutDialog = false
                        navController.navigate(Screen.SignIn) {
                            popUpTo(Screen.ChatList) { inclusive = true }
                            launchSingleTop = true
                        }
                    },
                )
            },
        )
    }
}
