
@file:OptIn(ExperimentalMaterial3Api::class)

package com.android.internship.presentation.screens.chat

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.android.internship.di.AppContainer
import com.android.internship.presentation.components.MessageItem
import com.android.internship.presentation.components.chat.*
import com.android.internship.presentation.screens.chat.components.TypingIndicatorComponent
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
                val memberAvatars = uiState.userMap.values.mapNotNull { it.avatar }
                ChatTopBar(
                    room = room,
                    memberAvatars = memberAvatars,
                    onBackClick = { navController.popBackStack() },
                )
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .imePadding(),
        ) {
            if (uiState.isLoading) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                LazyColumn(
                    state = listState,
                    modifier = Modifier.weight(1f).fillMaxWidth(),
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
                                TimeHeaderComponent(timestamp = messageItem.timestamp)
                            }
                            is MessageItem.MessageBubbles -> {
                                if (!messageItem.isFromMe) {
                                    LaunchedEffect(messageItem.message.mid) {
                                        viewModel.markAsSeen(messageItem.message.mid)
                                    }
                                }
                                MessageBubbleComponent(
                                    item = messageItem,
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
                        // Logic mở emoji picker
                    },
                )
            }
        }
    }
}
