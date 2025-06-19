package com.android.internship.presentation.screens.chat

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.android.internship.di.AppContainer
import com.android.internship.presentation.components.CommonProgressIndicator
import com.android.internship.presentation.components.chat.ChatTopBar
import com.android.internship.presentation.components.chat.EmptyChatComponent
import com.android.internship.presentation.components.chat.MessageBubbleComponent
import com.android.internship.presentation.components.chat.MessageInputComponent
import com.android.internship.presentation.components.chat.NetworkStatusBanner
import com.android.internship.presentation.components.chat.TimeHeaderComponent
import com.android.internship.presentation.components.chat.TypingIndicatorComponent
import com.android.internship.presentation.navigation.Screen
import java.time.ZoneOffset
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

@Composable
fun ChatScreen(
    navController: NavController,
    appContainer: AppContainer,
    isNetworkAvailable: Boolean,
) {
    val viewModel: ChatViewModel = viewModel(
        factory = ChatViewModelFactory(
            appContainer = appContainer,
        ),
    )

    val uiState by viewModel.uiState.collectAsState()
    val messageText by viewModel.messageText.collectAsState()
    val userRoom by viewModel.userRoom.collectAsState()
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    var isEmojiPickerVisible by remember { mutableStateOf(false) }

    LaunchedEffect(listState, uiState.messages.size, uiState.canLoadMore, uiState.isLoadingMore) {
        snapshotFlow {
            val layoutInfo = listState.layoutInfo
            val visibleItems = layoutInfo.visibleItemsInfo
            val totalItems = layoutInfo.totalItemsCount

            if (totalItems == 0 || !uiState.canLoadMore || uiState.isLoadingMore) {
                return@snapshotFlow false
            }

            val lastVisibleIndex = visibleItems.lastOrNull()?.index ?: -1
            val threshold = 3
            val shouldLoadMore = lastVisibleIndex >= totalItems - threshold
            shouldLoadMore
        }
            .distinctUntilChanged()
            .filter { it }
            .collect {
                viewModel.loadMoreMessages()
            }
    }

    uiState.errorMessage?.let { error ->
        LaunchedEffect(error) {
            snackbarHostState.showSnackbar(message = error)
            viewModel.clearError()
        }
    }

    LaunchedEffect(uiState.messages.isNotEmpty(), uiState.isLoading) {
        if (uiState.messages.isNotEmpty() && !uiState.isLoading) {
            listState.scrollToItem(0)
        }
    }

    LaunchedEffect(uiState.messages) {
        if (uiState.messages.isNotEmpty()) {
            val layoutInfo = listState.layoutInfo
            val isNearBottom = layoutInfo.visibleItemsInfo.any { it.index <= 2 }

            if (isNearBottom) {
                coroutineScope.launch {
                    listState.animateScrollToItem(0)
                }
            }
        }
    }

    Scaffold(
        topBar = {
            ChatTopBar(
                title = uiState.topBarTitle,
                subtitle = uiState.topBarSubtitle,
                avatarUrls = uiState.topBarAvatarUrls,
                isMuted = userRoom?.mute == true,
                isBlocked = userRoom?.isBlocked == true,
                onBackClick = {
                    navController.navigate(Screen.ChatList) {
                        popUpTo(Screen.Chat(viewModel.roomId)) { inclusive = true }
                        launchSingleTop = true
                    }
                },
                onBlockClick = {
                },
                onMuteClick = { duration ->
                    viewModel.muteUser(duration = duration)
                },
                isGroup = uiState.isGroup,
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        contentWindowInsets = WindowInsets.ime,
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
        ) {
            if (uiState.isLoading && uiState.messages.isEmpty()) {
                CommonProgressIndicator()
            } else {
                Box(modifier = Modifier.weight(1f)) {
                    LazyColumn(
                        state = listState,
                        modifier = Modifier.fillMaxSize(),
                        reverseLayout = true,
                        contentPadding = PaddingValues(
                            horizontal = 4.dp,
                            vertical = 8.dp,
                        ),
                        verticalArrangement = Arrangement.spacedBy(4.dp, Alignment.Bottom),
                        userScrollEnabled = !isEmojiPickerVisible,
                    ) {
                        items(
                            items = uiState.messages,
                            key = { item ->
                                when (item) {
                                    is MessageItem.MessageBubbles -> "msg_${item.message.mid}"
                                    is MessageItem.TimeHeader -> "time_${item.timestamp.toEpochSecond(ZoneOffset.UTC)}"
                                    is MessageItem.TypingIndicator -> "typing_indicator"
                                }
                            },
                        ) { messageItem ->
                            when (messageItem) {
                                is MessageItem.TimeHeader -> {
                                    TimeHeaderComponent(item = messageItem)
                                }
                                is MessageItem.MessageBubbles -> {
                                    MessageBubbleComponent(
                                        item = messageItem,
                                        currentUserAvatarUrl = uiState.currentUser?.avatar,
                                        onMessageClick = {
                                            viewModel.toggleSeenByVisibility(messageItem.message.mid)
                                        },
                                    )
                                }
                                is MessageItem.TypingIndicator -> {
                                    TypingIndicatorComponent(item = messageItem)
                                }
                            }
                        }
                    }

                    // Show empty state
                    if (uiState.messages.isEmpty() && !uiState.isLoading) {
                        EmptyChatComponent()
                    }
                }

                MessageInputComponent(
                    messageText = messageText,
                    onMessageChange = viewModel::onMessageChange,
                    onSendMessage = viewModel::sendMessage,
                    onEmojiClick = { isEmojiPickerVisible = !isEmojiPickerVisible },
                    onEmojiPickerVisibilityChange = { isVisible -> isEmojiPickerVisible = isVisible },
                )
            }
        }

        NetworkStatusBanner(
            isNetworkAvailable = isNetworkAvailable,
            isRefreshing = uiState.isRefreshing,
            onRefreshClick = viewModel::refreshData,
            modifier = Modifier.padding(top = 50.dp),
        )
    }
}
