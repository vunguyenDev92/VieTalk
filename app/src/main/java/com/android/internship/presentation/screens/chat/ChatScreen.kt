@file:OptIn(ExperimentalMaterial3Api::class)

package com.android.internship.presentation.screens.chat

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.android.internship.di.AppContainer
import com.android.internship.presentation.components.MessageItem
import com.android.internship.presentation.components.chat.ChatTopBar
import com.android.internship.presentation.components.chat.MessageBubbleComponent
import com.android.internship.presentation.components.chat.MessageInputComponent
import com.android.internship.presentation.components.chat.TimeHeaderComponent
import com.android.internship.presentation.components.chat.TypingIndicatorComponent
import kotlinx.coroutines.launch
@Composable
fun ChatScreen(
    navController: NavController,
) {
    val context = LocalContext.current
    val appContainer = remember { AppContainer(context) }

    val viewModel: ChatViewModel = viewModel(
        factory = ChatViewModelFactory(
            authRepository = appContainer.authRepository,
            roomRepository = appContainer.roomRepository,
        ),
    )

    val uiState by viewModel.uiState.collectAsState()
    val messageText by viewModel.messageText.collectAsState()

    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    var isEmojiPickerVisible by remember { mutableStateOf(false) }

    val imePadding = WindowInsets.ime.asPaddingValues()
    val customKeyboardPadding by remember {
        derivedStateOf {
            if (imePadding.calculateBottomPadding() > 0.dp) {
                21.dp
            } else {
                0.dp
            }
        }
    }

    uiState.errorMessage?.let { error ->
        LaunchedEffect(error) {
            snackbarHostState.showSnackbar(message = error)
            viewModel.clearError()
        }
    }

    LaunchedEffect(uiState.messages.size) {
        if (uiState.messages.isNotEmpty()) {
            coroutineScope.launch {
                listState.animateScrollToItem(0)
            }
        }
    }

    Scaffold(
        topBar = {
            uiState.room?.let { room ->
                ChatTopBar(
                    title = uiState.topBarTitle,
                    subtitle = uiState.topBarSubtitle,
                    avatarUrls = uiState.topBarAvatarUrls,
                    isSubtitleActive = uiState.isPeerActive,
                    onBackClick = { navController.popBackStack() },
                    onCallClick = { /* ... */ },
                    onMoreClick = { /* ... */ },
                )
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
        ) {
            if (uiState.isLoading) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    reverseLayout = true,
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.Bottom),
                ) {
                    items(
                        items = uiState.messages,
                        key = { item ->
                            when (item) {
                                is MessageItem.MessageBubbles -> "msg_${item.message.mid}"
                                is MessageItem.TimeHeader -> "time_${item.timestamp.toEpochSecond(java.time.ZoneOffset.UTC)}"
                                is MessageItem.TypingIndicator -> "typing_indicator"
                            }
                        },
                    ) { messageItem ->
                        when (messageItem) {
                            is MessageItem.TimeHeader -> {
                                TimeHeaderComponent(item = messageItem)
                            }
                            is MessageItem.MessageBubbles -> {
                                if (!messageItem.isFromMe) {
                                    LaunchedEffect(messageItem.message.mid) {
                                        viewModel.markAsSeen(messageItem.message)
                                    }
                                }
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

                MessageInputComponent(
                    messageText = messageText,
                    onMessageChange = viewModel::onMessageChange,
                    onSendMessage = viewModel::sendMessage,
                    onEmojiClick = {
                        isEmojiPickerVisible = !isEmojiPickerVisible
                    },
                    onEmojiPickerVisibilityChange = { isVisible ->
                        isEmojiPickerVisible = isVisible
                    },
                    modifier = Modifier.padding(bottom = customKeyboardPadding),
                )
            }
        }
    }
}
