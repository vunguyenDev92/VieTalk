package com.android.internship.presentation.screens.chat

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.android.internship.R
import com.android.internship.di.AppContainer
import com.android.internship.presentation.components.CommonProgressIndicator
import com.android.internship.presentation.components.MessageItem
import com.android.internship.presentation.components.chat.ChatTopBar
import com.android.internship.presentation.components.chat.EmptyChatComponent
import com.android.internship.presentation.components.chat.MessageBubbleComponent
import com.android.internship.presentation.components.chat.MessageInputComponent
import com.android.internship.presentation.components.chat.NetworkStatusBanner
import com.android.internship.presentation.components.chat.TimeHeaderComponent
import com.android.internship.presentation.components.chat.TypingIndicatorComponent
import com.android.internship.presentation.theme.robotoFamily
import java.time.ZoneOffset
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
            connectivityObserver = appContainer.connectivityObserver,
            userRepository = appContainer.userRepository,
            messageRepository = appContainer.messageRepository,
            userRoomRepository = appContainer.userRoomRepository,
        ),
    )

    val uiState by viewModel.uiState.collectAsState()
    val messageText by viewModel.messageText.collectAsState()
    val userRoom by viewModel.userRoom.collectAsState()
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    var isEmojiPickerVisible by remember { mutableStateOf(false) }

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
            ChatTopBar(
                title = uiState.topBarTitle,
                subtitle = uiState.topBarSubtitle,
                avatarUrls = uiState.topBarAvatarUrls,
                isSubtitleActive = uiState.isPeerActive,
                isMuted = userRoom?.mute == true,
                isBlocked = userRoom?.isBlocked == true,
                isOtherBlocked = uiState.isOtherUserBlocked,
                onBackClick = { navController.popBackStack() },
                onBlockClick = {
                    viewModel.toggleBlockState()
                },
                onMuteClick = { duration ->
                },
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
            if (uiState.isLoading) {
                CommonProgressIndicator()
            } else {
                Box(modifier = Modifier.weight(1f)) {
                    if (uiState.messages.isEmpty()) {
                        EmptyChatComponent()
                    } else {
                        LazyColumn(
                            state = listState,
                            modifier = Modifier.fillMaxSize(),
                            reverseLayout = true,
                            contentPadding = PaddingValues(
                                horizontal = 4.dp,
                                vertical = 4.dp,
                            ),
                            verticalArrangement = Arrangement.spacedBy(0.dp, Alignment.Bottom),
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
                                        if (!messageItem.isFromMe) {
                                            LaunchedEffect(messageItem.message.mid) {
                                                viewModel.markAsSeen(messageItem.message.mid)
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
                    }
                }
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    if (uiState.room?.isGroup == false && userRoom?.isBlocked == false && uiState.isOtherUserBlocked) {
                        Text(
                            text = stringResource(R.string.description_isblocked),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 15.dp, top = 35.dp, bottom = 21.dp, end = 15.dp),
                            fontWeight = FontWeight.W400,
                            fontSize = 12.sp,
                            fontFamily = robotoFamily,
                            textAlign = TextAlign.Center,
                        )
                    } else if (userRoom?.isBlocked == true) {
                        Text(
                            text = stringResource(R.string.description_blocked).replace(
                                "\$title",
                                uiState.topBarTitle,
                            ),
                            modifier = Modifier
                                .padding(start = 30.dp, top = 35.dp, bottom = 21.dp),
                            fontWeight = FontWeight.W400,
                            fontSize = 12.sp,
                            fontFamily = robotoFamily,
                            textAlign = TextAlign.Center,
                            lineHeight = 18.sp,
                        )
                    } else {
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
                            modifier = Modifier,
                        )
                    }
                }
            }
        }

        NetworkStatusBanner(
            isNetworkAvailable = uiState.isNetworkAvailable,
            isRefreshing = uiState.isRefreshing,
            onRefreshClick = viewModel::refreshData,
            modifier = Modifier.padding(top = 50.dp),
        )
    }
}
