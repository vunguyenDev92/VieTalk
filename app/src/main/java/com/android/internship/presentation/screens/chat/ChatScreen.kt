 @file:OptIn(ExperimentalMaterial3Api::class)

 package com.android.internship.presentation.screens.chat

 import androidx.compose.foundation.layout.Arrangement
 import androidx.compose.foundation.layout.Box
 import androidx.compose.foundation.layout.Column
 import androidx.compose.foundation.layout.PaddingValues
 import androidx.compose.foundation.layout.fillMaxSize
 import androidx.compose.foundation.layout.fillMaxWidth
 import androidx.compose.foundation.layout.imePadding
 import androidx.compose.foundation.layout.padding
 import androidx.compose.foundation.lazy.LazyColumn
 import androidx.compose.foundation.lazy.items
 import androidx.compose.foundation.lazy.rememberLazyListState
 import androidx.compose.material3.CircularProgressIndicator
 import androidx.compose.material3.ExperimentalMaterial3Api
 import androidx.compose.material3.MaterialTheme
 import androidx.compose.material3.Snackbar
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
 import androidx.compose.ui.tooling.preview.Preview
 import androidx.compose.ui.unit.dp
 import androidx.lifecycle.viewmodel.compose.viewModel
 import androidx.navigation.NavController
 import com.android.internship.data.model.Message
 import com.android.internship.data.model.Room
 import com.android.internship.data.model.UserRoom
 import com.android.internship.presentation.components.MessageItem
 import com.android.internship.presentation.components.chat.ChatTopBar
 import com.android.internship.presentation.components.chat.MessageBubbleComponent
 import com.android.internship.presentation.components.chat.MessageInputComponent
 import com.android.internship.presentation.components.chat.MessageTimeHeaderComponent
 import com.android.internship.presentation.components.chat.TimeHeaderComponent
 import com.android.internship.presentation.components.utils.processMessagesToItems
 import com.android.internship.presentation.screens.chat.components.TypingIndicatorComponent
 import kotlinx.coroutines.launch

 @Composable
 fun ChatScreen(
//    navController: NavController,
//    roomId: String,
//    currentUserId: String,
//    modifier: Modifier = Modifier,
//    onBackClick: (() -> Unit)? = null,
//    onCallClick: (() -> Unit)? = null,
//    onMoreClick: (() -> Unit)? = null,
//    onEmojiClick: (() -> Unit)? = null,
//    viewModel: MessageViewModel = viewModel(
//        factory = MessageViewModel.factory(
//            context = navController.context,
//            roomId = roomId,
//            currentUserId = currentUserId,
//        ),
//    ),
 ) {
//    val uiState by viewModel.uiState.collectAsState()
//    val messageText by viewModel.messageText.collectAsState()
//    val selectedMessageId by viewModel.selectedMessageId.collectAsState()
//
//    val listState = rememberLazyListState()
//    val coroutineScope = rememberCoroutineScope()
//    val snackbarHostState = remember { SnackbarHostState() }
//
//    // Calculate message items - REVERSE the order for bottom-up display
//    val messageItems = remember(uiState.room, currentUserId) {
//        uiState.room?.let { room ->
//            processRoomToMessageItems(room, currentUserId).reversed()
//        } ?: emptyList()
//    }
//
//    LaunchedEffect(uiState.room?.messageChats?.size) {
//        if (messageItems.isNotEmpty()) {
//            coroutineScope.launch {
//                listState.animateScrollToItem(0)
//            }
//        }
//    }
//
//    LaunchedEffect(uiState.error) {
//        uiState.error?.let { error ->
//            snackbarHostState.showSnackbar(error)
//            viewModel.clearError()
//        }
//    }
//
//    Box(modifier = modifier.fillMaxSize()) {
//        if (uiState.isLoading) {
//            Box(
//                modifier = Modifier.fillMaxSize(),
//                contentAlignment = Alignment.Center,
//            ) {
//                CircularProgressIndicator()
//            }
//        } else if (uiState.room != null) {
//            Column(
//                modifier = Modifier
//                    .fillMaxSize(),
//            ) {
//                ChatTopBar(
//                    avatarUrl = uiState.room!!.avatar ?: "https://via.placeholder.com/150",
//                    userName = uiState.room!!.name ?: "Chat",
//                    userStatus = "Active now",
//                    onBackClick = (onBackClick ?: { navController.popBackStack() }) as () -> Unit,
//                    onCallClick = onCallClick ?: {},
//                    onMoreClick = onMoreClick ?: {},
//                )
//
//                LazyColumn(
//                    state = listState,
//                    modifier = Modifier
//                        .weight(1f)
//                        .fillMaxWidth()
//                        .padding(horizontal = 16.dp),
//                    reverseLayout = true,
//                    verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.Bottom),
//                    contentPadding = PaddingValues(vertical = 16.dp),
//                ) {
//                    items(messageItems) { messageItem ->
//                        when (messageItem) {
//                            is MessageItem.TimeHeader -> {
//                                TimeHeaderComponent(timestamp = messageItem.timestamp)
//                            }
//                            is MessageItem.MessageBubbles -> {
//                                Column {
//                                    MessageBubbleComponent(
//                                        message = messageItem.message,
//                                        isFromMe = messageItem.isFromMe,
//                                        senderName = messageItem.senderName,
//                                        avatarRes = messageItem.avatarRes,
//                                        seenBy = messageItem.seenBy,
//                                        isGroupChat = uiState.room!!.isGroup,
//                                        totalUsersInGroup = messageItem.totalUsersInGroup,
//                                        currentUserId = currentUserId,
//                                        onMessageClick = { messageId ->
//                                            viewModel.selectMessage(messageId)
//                                        },
//                                    )
//                                    if (selectedMessageId == messageItem.message.mid) {
//                                        MessageTimeHeaderComponent(message = messageItem.message)
//                                    }
//                                }
//                            }
//                            is MessageItem.TypingIndicator -> {
//                                TypingIndicatorComponent(
//                                    senderName = messageItem.userName,
//                                    avatarRes = messageItem.avatarRes,
//                                    isGroupChat = uiState.room!!.isGroup,
//                                    totalUsersInGroup = messageItem.totalUsersInGroup,
//                                )
//                            }
//                        }
//                    }
//                }
//
//                // Message input
//                MessageInputComponent(
//                    messageText = messageText,
//                    onMessageChange = { viewModel.updateMessageText(it) },
//                    onSendMessage = { viewModel.sendMessage() },
//                    isEnabled = !uiState.isLoading,
//                    onEmojiClick = onEmojiClick ?: {
//                    },
//                )
//            }
//        } else {
//            Box(
//                modifier = Modifier.fillMaxSize(),
//                contentAlignment = Alignment.Center,
//            ) {
//                Text(
//                    text = "Failed to load chat room",
//                    style = MaterialTheme.typography.bodyLarge,
//                    color = MaterialTheme.colorScheme.error,
//                )
//            }
//        }
//
//        SnackbarHost(
//            hostState = snackbarHostState,
//            modifier = Modifier.align(Alignment.BottomCenter),
//        ) { snackbarData ->
//            Snackbar(
//                snackbarData = snackbarData,
//                containerColor = MaterialTheme.colorScheme.errorContainer,
//                contentColor = MaterialTheme.colorScheme.onErrorContainer,
//            )
//        }
//    }
// }
//
// @Composable
// fun ChatScreenPreview(
//    room: Room,
//    currentUserId: String,
//    onBackClick: () -> Unit = {},
//    onCallClick: () -> Unit = {},
//    onMoreClick: () -> Unit = {},
//    onEmojiClick: () -> Unit = {},
//    onSendMessage: (String) -> Unit = {},
//    modifier: Modifier = Modifier,
// ) {
//    var messageText by remember { mutableStateOf("") }
//    var selectedMessageId by remember { mutableStateOf<String?>(null) }
//
//    val messageItems = remember(room, currentUserId) {
//        processRoomToMessageItems(room, currentUserId).reversed()
//    }
//
//    val listState = rememberLazyListState()
//    val coroutineScope = rememberCoroutineScope()
//
//    LaunchedEffect(room.messageChats.size) {
//        if (messageItems.isNotEmpty()) {
//            coroutineScope.launch {
//                listState.animateScrollToItem(0)
//            }
//        }
//    }
//
//    Column(
//        modifier = modifier
//            .fillMaxSize()
//            .imePadding(),
//    ) {
//        ChatTopBar(
//            avatarUrl = room.avatar ?: "https://via.placeholder.com/150",
//            userName = room.name ?: "Chat",
//            userStatus = "Active now",
//            onBackClick = onBackClick,
//            onCallClick = onCallClick,
//            onMoreClick = onMoreClick,
//        )
//
//        LazyColumn(
//            state = listState,
//            modifier = Modifier
//                .weight(1f)
//                .fillMaxWidth()
//                .padding(horizontal = 16.dp),
//            reverseLayout = true, // This makes messages start from bottom
//            verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.Bottom),
//            contentPadding = PaddingValues(vertical = 16.dp),
//        ) {
//            items(messageItems) { messageItem ->
//                when (messageItem) {
//                    is MessageItem.TimeHeader -> {
//                        TimeHeaderComponent(timestamp = messageItem.timestamp)
//                    }
//                    is MessageItem.MessageBubbles -> {
//                        Column {
//                            MessageBubbleComponent(
//                                message = messageItem.message,
//                                isFromMe = messageItem.isFromMe,
//                                senderName = messageItem.senderName,
//                                avatarRes = messageItem.avatarRes,
//                                seenBy = messageItem.seenBy,
//                                isGroupChat = room.isGroup,
//                                totalUsersInGroup = messageItem.totalUsersInGroup,
//                                currentUserId = currentUserId,
//                                onMessageClick = { messageId ->
//                                    selectedMessageId = if (selectedMessageId == messageId) {
//                                        null
//                                    } else {
//                                        messageId
//                                    }
//                                },
//                            )
//                            if (selectedMessageId == messageItem.message.mid) {
//                                MessageTimeHeaderComponent(message = messageItem.message)
//                            }
//                        }
//                    }
//                    is MessageItem.TypingIndicator -> {
//                        TypingIndicatorComponent(
//                            senderName = messageItem.userName,
//                            avatarRes = messageItem.avatarRes,
//                            isGroupChat = room.isGroup,
//                            totalUsersInGroup = messageItem.totalUsersInGroup,
//                        )
//                    }
//                }
//            }
//        }
//
//        MessageInputComponent(
//            messageText = messageText,
//            onMessageChange = { messageText = it },
//            onSendMessage = {
//                if (messageText.isNotBlank()) {
//                    onSendMessage(messageText.trim())
//                    messageText = ""
//                }
//            },
//            onEmojiClick = onEmojiClick,
//            placeholder = "Message",
//            isEnabled = true,
//        )
//    }
// }
//
// @Preview(showBackground = true, heightDp = 800)
// @Composable
// fun ChatScreenPreviewDemo() {
//    val mockRoom = Room(
//        rid = "room_1",
//        isGroup = true,
//        users = listOf(
//            UserRoom(
//                rid = "room_1",
//                uid = "eliza_id",
//                mute = false,
//                turnOnTime = null,
//                lastSeenMessages = "1",
//            ),
//            UserRoom(
//                rid = "room_1",
//                uid = "me_id",
//                mute = false,
//                turnOnTime = null,
//                lastSeenMessages = "2",
//            ),
//            UserRoom(
//                rid = "room_1",
//                uid = "user_3",
//                mute = false,
//                turnOnTime = null,
//                lastSeenMessages = "1",
//            ),
//        ),
//        messageChats = listOf(
//            Message(
//                mid = "1",
//                uid = "eliza_id",
//                content = "Hey there! How are you doing?",
//                time = "2025-06-03T10:03:00",
//            ),
//            Message(
//                mid = "2",
//                uid = "me_id",
//                content = "Hi! I'm doing great, thanks for asking! 😊",
//                time = "2025-06-03T12:06:00",
//            ),
//            Message(
//                mid = "3",
//                uid = "user_3",
//                content = "Hello everyone! This is a longer message to test how the chat bubbles look with more content.",
//                time = "2025-06-03T14:08:00",
//            ),
//        ),
//        isTyping = listOf("eliza_id"),
//        avatar = null,
//        name = "Group Chat",
//    )
//
//    MaterialTheme {
//        ChatScreenPreview(
//            room = mockRoom,
//            currentUserId = "me_id",
//        )
//    }
 }
