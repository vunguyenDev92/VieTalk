// package com.android.internship.presentation.screens.chat
//
// import android.content.Context
// import androidx.lifecycle.ViewModel
// import androidx.lifecycle.ViewModelProvider
// import androidx.lifecycle.viewModelScope
// import com.android.internship.data.model.MessageChat
// import com.android.internship.data.model.Room
// import com.android.internship.data.model.UserRoom
// import kotlinx.coroutines.flow.MutableStateFlow
// import kotlinx.coroutines.flow.StateFlow
// import kotlinx.coroutines.flow.asStateFlow
// import kotlinx.coroutines.launch
// import java.time.LocalDateTime
// import java.time.format.DateTimeFormatter
//
// data class ChatUiState(
// 	val room: Room? = null,
// 	val isLoading: Boolean = false,
// 	val error: String? = null,
// 	val isTyping: Boolean = false,
// 					  )
//
// class MessageViewModel(
// 	private val roomId: String,
// 	private val currentUserId: String,
// 					  ) : ViewModel() {
//
// 	private val _uiState = MutableStateFlow(ChatUiState())
// 	val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()
//
// 	private val _messageText = MutableStateFlow("")
// 	val messageText: StateFlow<String> = _messageText.asStateFlow()
//
// 	private val _selectedMessageId = MutableStateFlow<String?>(null)
// 	val selectedMessageId: StateFlow<String?> = _selectedMessageId.asStateFlow()
//
// 	// Dummy users for group chat
// 	private val otherUserId = "friend_user_id"
// 	private val otherUserName = "Bạn Chat"
//
// 	init {
// 		loadRoom()
// 	}
//
// 	fun loadRoom() {
// 		viewModelScope.launch {
// 			_uiState.value = _uiState.value.copy(isLoading = true, error = null)
//
// 			try {
// 				// Create dummy room data with 2 users
// 				val mockRoom = Room(
// 					rid = roomId,
// 					isGroup = false, // 2 người nên set false, hoặc true nếu muốn hiển thị như group
// 					users = listOf(
// 						UserRoom(
// 							rid = roomId,
// 							uid = currentUserId,
// 							mute = false,
// 							turnOnTime = null,
// 							lastSeenMessages = "3",
// 								),
// 						UserRoom(
// 							rid = roomId,
// 							uid = otherUserId,
// 							mute = false,
// 							turnOnTime = null,
// 							lastSeenMessages = "2",
// 								),
// 								  ),
// 					messageChats = mutableListOf(
// 						MessageChat(
// 							mid = "1",
// 							uid = otherUserId,
// 							content = "Chào bạn! Bạn khỏe không?",
// 							time = LocalDateTime.now().minusHours(2).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
// 								   ),
// 						MessageChat(
// 							mid = "2",
// 							uid = currentUserId,
// 							content = "Chào! Tôi khỏe, cảm ơn bạn! 😊",
// 							time = LocalDateTime.now().minusHours(1).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
// 								   ),
// 						MessageChat(
// 							mid = "3",
// 							uid = otherUserId,
// 							content = "Hôm nay bạn có rảnh không? Đi uống cà phê nhé!",
// 							time = LocalDateTime.now().minusMinutes(30).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
// 								   ),
// 												),
// 					isTyping = emptyList(),
// 					avatar = "https://via.placeholder.com/150",
// 					name = otherUserName,
// 								   )
//
// 				_uiState.value = _uiState.value.copy(
// 					room = mockRoom,
// 					isLoading = false,
// 													)
// 			} catch (e: Exception) {
// 				_uiState.value = _uiState.value.copy(
// 					isLoading = false,
// 					error = e.message ?: "Có lỗi xảy ra khi tải phòng chat",
// 													)
// 			}
// 		}
// 	}
//
// 	fun updateMessageText(text: String) {
// 		_messageText.value = text
//
// 		// Handle typing indicator
// 		if (text.isNotBlank() && !_uiState.value.isTyping) {
// 			startTyping()
// 		} else if (text.isBlank() && _uiState.value.isTyping) {
// 			stopTyping()
// 		}
// 	}
//
// 	fun sendMessage() {
// 		val content = _messageText.value.trim()
// 		if (content.isBlank()) return
//
// 		viewModelScope.launch {
// 			try {
// 				// Create new message
// 				val newMessage = MessageChat(
// 					mid = System.currentTimeMillis().toString(),
// 					uid = currentUserId,
// 					content = content,
// 					time = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
// 											)
//
// 				// Update room with new message
// 				_uiState.value.room?.let { currentRoom ->
// 					val updatedMessages = currentRoom.messageChats.toMutableList()
// 					updatedMessages.add(newMessage)
//
// 					val updatedRoom = currentRoom.copy(
// 						messageChats = updatedMessages
// 													  )
//
// 					_uiState.value = _uiState.value.copy(room = updatedRoom)
// 				}
//
// 				// Clear message text
// 				_messageText.value = ""
//
// 				// Stop typing indicator
// 				stopTyping()
//
// 				// Simulate other user response (optional - for demo purposes)
// 				simulateOtherUserResponse()
//
// 			} catch (e: Exception) {
// 				_uiState.value = _uiState.value.copy(
// 					error = e.message ?: "Gửi tin nhắn thất bại",
// 													)
// 			}
// 		}
// 	}
//
// 	private fun simulateOtherUserResponse() {
// 		// Simulate other user typing and responding after a delay
// 		viewModelScope.launch {
// 			kotlinx.coroutines.delay(2000) // Wait 2 seconds
//
// 			// Show other user is typing
// 			_uiState.value.room?.let { currentRoom ->
// 				val updatedRoom = currentRoom.copy(
// 					isTyping = listOf(otherUserId)
// 												  )
// 				_uiState.value = _uiState.value.copy(room = updatedRoom)
// 			}
//
// 			kotlinx.coroutines.delay(3000) // Type for 3 seconds
//
// 			// Send response message
// 			val responses = listOf(
// 				"Được rồi!",
// 				"Tôi hiểu rồi 👍",
// 				"Cảm ơn bạn!",
// 				"Nghe hay đấy!",
// 				"Haha, thú vị quá!",
// 				"Tôi cũng nghĩ vậy",
// 				"Chắc chắn rồi!"
// 								  )
//
// 			val responseMessage = MessageChat(
// 				mid = System.currentTimeMillis().toString(),
// 				uid = otherUserId,
// 				content = responses.random(),
// 				time = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
// 											 )
//
// 			_uiState.value.room?.let { currentRoom ->
// 				val updatedMessages = currentRoom.messageChats.toMutableList()
// 				updatedMessages.add(responseMessage)
//
// 				val updatedRoom = currentRoom.copy(
// 					messageChats = updatedMessages,
// 					isTyping = emptyList() // Stop typing
// 												  )
//
// 				_uiState.value = _uiState.value.copy(room = updatedRoom)
// 			}
// 		}
// 	}
//
// 	fun selectMessage(messageId: String?) {
// 		_selectedMessageId.value = if (_selectedMessageId.value == messageId) {
// 			null
// 		} else {
// 			messageId
// 		}
// 	}
//
// 	private fun startTyping() {
// 		if (_uiState.value.isTyping) return
// 		_uiState.value = _uiState.value.copy(isTyping = true)
// 	}
//
// 	private fun stopTyping() {
// 		if (!_uiState.value.isTyping) return
// 		_uiState.value = _uiState.value.copy(isTyping = false)
// 	}
//
// 	fun clearError() {
// 		_uiState.value = _uiState.value.copy(error = null)
// 	}
//
// 	companion object {
// 		fun factory(
// 			context: Context,
// 			roomId: String,
// 			currentUserId: String,
// 				   ) = object : ViewModelProvider.Factory {
// 			@Suppress("UNCHECKED_CAST")
// 			override fun <T : ViewModel> create(modelClass: Class<T>): T {
// 				if (modelClass.isAssignableFrom(MessageViewModel::class.java)) {
// 					return MessageViewModel(
// 						roomId = roomId,
// 						currentUserId = currentUserId,
// 										   ) as T
// 				}
// 				throw IllegalArgumentException("Unknown ViewModel class")
// 			}
// 		}
// 	}
// }
//
//package com.android.internship.presentation.screens.chat
//
//import android.content.Context
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.ViewModelProvider
//import androidx.lifecycle.viewModelScope
//import com.android.internship.data.model.Message
//import com.android.internship.data.model.Room
//import com.android.internship.data.model.UserRoom
//import java.time.LocalDateTime
//import java.time.format.DateTimeFormatter
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.flow.asStateFlow
//import kotlinx.coroutines.launch
//
//data class ChatUiState(
//    val room: Room? = null,
//    val isLoading: Boolean = false,
//    val error: String? = null,
//    val isTyping: Boolean = false,
//)
//
//class MessageViewModel(
//    private val roomId: String,
//    private val currentUserId: String,
//) : ViewModel() {
//
//    private val _uiState = MutableStateFlow(ChatUiState())
//    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()
//
//    private val _messageText = MutableStateFlow("")
//    val messageText: StateFlow<String> = _messageText.asStateFlow()
//
//    private val _selectedMessageId = MutableStateFlow<String?>(null)
//    val selectedMessageId: StateFlow<String?> = _selectedMessageId.asStateFlow()
//
//    private val user2Id = "Alice"
//    private val user3Id = "Bob"
//
//    private val isGroupChat = roomId.contains("group") || roomId.contains("3")
//
//    init {
//        loadRoom()
//    }

    fun loadRoom() {
//        viewModelScope.launch {
//            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
//
//            try {
//                val mockRoom = if (isGroupChat) {
////                    createGroupChatRoom()
//                } else {
//                    createPrivateChatRoom()
//                }
//
//                _uiState.value = _uiState.value.copy(
//                    room = mockRoom,
//                    isLoading = false,
//                )
//            } catch (e: Exception) {
//                _uiState.value = _uiState.value.copy(
//                    isLoading = false,
//                    error = e.message ?: "Có lỗi xảy ra khi tải phòng chat",
//                )
//            }
//        }
    }

//    private fun createPrivateChatRoom(): Room {
//        return Room(
//            rid = roomId,
//            isGroup = false,
//            users = listOf(
//                UserRoom(
//                    rid = roomId,
//                    uid = currentUserId,
//                    mute = false,
//                    turnOnTime = null,
//                    lastSeenMessages = "3",
//                ),
//                UserRoom(
//                    rid = roomId,
//                    uid = user2Id,
//                    mute = false,
//                    turnOnTime = null,
//                    lastSeenMessages = "2",
//                ),
//            ),
//            messageChats = mutableListOf(
//                Message(
//                    mid = "1",
//                    uid = user2Id,
//                    content = "Chào bạn! Bạn khỏe không?",
//                    time = LocalDateTime.now().minusHours(2).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
//                ),
//                Message(
//                    mid = "2",
//                    uid = currentUserId,
//                    content = "Chào! Tôi khỏe, cảm ơn bạn! 😊",
//                    time = LocalDateTime.now().minusHours(1).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
//                ),
//                Message(
//                    mid = "3",
//                    uid = user2Id,
//                    content = "Hôm nay bạn có rảnh không? Đi uống cà phê nhé!",
//                    time = LocalDateTime.now().minusMinutes(30).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
//                ),
//            ),
//            isTyping = emptyList(),
//            avatar = "https://via.placeholder.com/150",
//            name = "Alice Nguyễn",
//        )
//    }
//
//    private fun createGroupChatRoom(): Room {
//        return Room(
//            rid = roomId,
//            isGroup = true,
//            users = listOf(
//                UserRoom(
//                    rid = roomId,
//                    uid = currentUserId,
//                    mute = false,
//                    turnOnTime = null,
//                    lastSeenMessages = "5",
//                ),
//                UserRoom(
//                    rid = roomId,
//                    uid = user2Id,
//                    mute = false,
//                    turnOnTime = null,
//                    lastSeenMessages = "4",
//                ),
//                UserRoom(
//                    rid = roomId,
//                    uid = user3Id,
//                    mute = false,
//                    turnOnTime = null,
//                    lastSeenMessages = "3",
//                ),
//            ),
//            messageChats = mutableListOf(
//                Message(
//                    mid = "1",
//                    uid = user2Id,
//                    content = "Chào mọi người! 👋",
//                    time = LocalDateTime.now().minusHours(3).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
//                ),
//                Message(
//                    mid = "2",
//                    uid = user3Id,
//                    content = "Chào Alice! Hôm nay thế nào?",
//                    time = LocalDateTime.now().minusHours(2).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
//                ),
//                Message(
//                    mid = "3",
//                    uid = currentUserId,
//                    content = "Chào cả nhóm! Tôi vừa vào đây 😊",
//                    time = LocalDateTime.now().minusHours(1).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
//                ),
//                Message(
//                    mid = "4",
//                    uid = user2Id,
//                    content = "Chào bạn! Cuối tuần mình đi đâu đó nhé!",
//                    time = LocalDateTime.now().minusMinutes(45).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
//                ),
//                Message(
//                    mid = "5",
//                    uid = user3Id,
//                    content = "Ý tưởng hay đấy! Đi Đà Nẵng thì sao? 🏖️",
//                    time = LocalDateTime.now().minusMinutes(20).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
//                ),
//            ),
//            isTyping = emptyList(),
//            avatar = null,
//            name = "Nhóm Bạn Thân",
//        )
//    }
//
//    fun updateMessageText(text: String) {
//        _messageText.value = text
//
//        if (text.isNotBlank() && !_uiState.value.isTyping) {
//            startTyping()
//        } else if (text.isBlank() && _uiState.value.isTyping) {
//            stopTyping()
//        }
//    }
//
//    fun sendMessage() {
//        val content = _messageText.value.trim()
//        if (content.isBlank()) return
//
//        viewModelScope.launch {
//            try {
//                // Create new message
//                val newMessage = Message(
//                    mid = System.currentTimeMillis().toString(),
//                    uid = currentUserId,
//                    content = content,
//                    time = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
//                )
//
//                _uiState.value.room?.let { currentRoom ->
//                    val updatedMessages = currentRoom.messageChats.toMutableList()
//                    updatedMessages.add(newMessage)
//
//                    val updatedRoom = currentRoom.copy(
//                        messageChats = updatedMessages,
//                    )
//
//                    _uiState.value = _uiState.value.copy(room = updatedRoom)
//                }
//
//                _messageText.value = ""
//
//                stopTyping()
//
//                simulateOtherUserResponse()
//            } catch (e: Exception) {
//                _uiState.value = _uiState.value.copy(
//                    error = e.message ?: "Gửi tin nhắn thất bại",
//                )
//            }
//        }
//    }
//
//    private fun simulateOtherUserResponse() {
//        if (!isGroupChat) {
//            simulatePrivateChatResponse()
//        } else {
//            simulateGroupChatResponse()
//        }
//    }
//
//    private fun simulatePrivateChatResponse() {
//        viewModelScope.launch {
//            kotlinx.coroutines.delay(2000)
//
//            _uiState.value.room?.let { currentRoom ->
//                val updatedRoom = currentRoom.copy(
//                    isTyping = listOf(user2Id),
//                )
//                _uiState.value = _uiState.value.copy(room = updatedRoom)
//            }
//
//            kotlinx.coroutines.delay(3000)
//
//            val responses = listOf(
//                "Được rồi!",
//                "Tôi hiểu rồi 👍",
//                "Cảm ơn bạn!",
//                "Nghe hay đấy!",
//                "Haha, thú vị quá!",
//                "Tôi cũng nghĩ vậy",
//                "Chắc chắn rồi!",
//            )
//
//            val responseMessage = Message(
//                mid = System.currentTimeMillis().toString(),
//                uid = user2Id,
//                content = responses.random(),
//                time = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
//            )
//
//            _uiState.value.room?.let { currentRoom ->
//                val updatedMessages = currentRoom.messageChats.toMutableList()
//                updatedMessages.add(responseMessage)
//
//                val updatedRoom = currentRoom.copy(
//                    messageChats = updatedMessages,
//                    isTyping = emptyList(),
//                )
//
//                _uiState.value = _uiState.value.copy(room = updatedRoom)
//            }
//        }
//    }
//
//    private fun simulateGroupChatResponse() {
//        viewModelScope.launch {
//            kotlinx.coroutines.delay(2000) // Wait 2 seconds
//
//            val respondingUserId = listOf(user2Id, user3Id).random()
//
//            _uiState.value.room?.let { currentRoom ->
//                val updatedRoom = currentRoom.copy(
//                    isTyping = listOf(respondingUserId),
//                )
//                _uiState.value = _uiState.value.copy(room = updatedRoom)
//            }
//
//            kotlinx.coroutines.delay(3000)
//
//            val responses = if (respondingUserId == user2Id) {
//                listOf(
//                    "Đồng ý! 👍",
//                    "Ý kiến hay đấy!",
//                    "Mình cũng nghĩ vậy",
//                    "Cùng làm nhé! 😊",
//                    "Tuyệt vời!",
//                    "Haha, đúng rồi!",
//                    "OK luôn!",
//                )
//            } else {
//                listOf(
//                    "Sounds good! 👌",
//                    "Tôi ủng hộ ý này",
//                    "Chill thôi! 😎",
//                    "Let's go!",
//                    "Hay quá bạn ơi!",
//                    "Tôi in luôn!",
//                    "Nice idea! 🔥",
//                )
//            }
//
//            val responseMessage = Message(
//                mid = System.currentTimeMillis().toString(),
//                uid = respondingUserId,
//                content = responses.random(),
//                time = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
//            )
//
//            _uiState.value.room?.let { currentRoom ->
//                val updatedMessages = currentRoom.messageChats.toMutableList()
//                updatedMessages.add(responseMessage)
//
//                val updatedRoom = currentRoom.copy(
//                    messageChats = updatedMessages,
//                    isTyping = emptyList(),
//                )
//
//                _uiState.value = _uiState.value.copy(room = updatedRoom)
//            }
//
//            if (kotlin.random.Random.nextFloat() < 0.3f) {
//                kotlinx.coroutines.delay(4000)
//                simulateSecondResponse(respondingUserId)
//            }
//        }
//    }
//
//    private fun simulateSecondResponse(firstResponderId: String) {
//        viewModelScope.launch {
//            val secondResponderId = if (firstResponderId == user2Id) user3Id else user2Id
//
//            _uiState.value.room?.let { currentRoom ->
//                val updatedRoom = currentRoom.copy(
//                    isTyping = listOf(secondResponderId),
//                )
//                _uiState.value = _uiState.value.copy(room = updatedRoom)
//            }
//
//            kotlinx.coroutines.delay(2000)
//
//            val followUpResponses = listOf(
//                "Mình cũng vậy!",
//                "+1 từ mình",
//                "Đúng rồi đó!",
//                "Totally agree!",
//                "Same here! 😄",
//                "Mình join luôn!",
//            )
//
//            val secondMessage = Message(
//                mid = System.currentTimeMillis().toString(),
//                uid = secondResponderId,
//                content = followUpResponses.random(),
//                time = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
//            )
//
//            _uiState.value.room?.let { currentRoom ->
//                val updatedMessages = currentRoom.messageChats.toMutableList()
//                updatedMessages.add(secondMessage)
//
//                val updatedRoom = currentRoom.copy(
//                    messageChats = updatedMessages,
//                    isTyping = emptyList(),
//                )
//
//                _uiState.value = _uiState.value.copy(room = updatedRoom)
//            }
//        }
//    }
//
//    fun selectMessage(messageId: String?) {
//        _selectedMessageId.value = if (_selectedMessageId.value == messageId) {
//            null
//        } else {
//            messageId
//        }
//    }
//
//    private fun startTyping() {
//        if (_uiState.value.isTyping) return
//        _uiState.value = _uiState.value.copy(isTyping = true)
//    }
//
//    private fun stopTyping() {
//        if (!_uiState.value.isTyping) return
//        _uiState.value = _uiState.value.copy(isTyping = false)
//    }
//
//    fun clearError() {
//        _uiState.value = _uiState.value.copy(error = null)
//    }
//
//    companion object {
//        fun factory(
//            context: Context,
//            roomId: String,
//            currentUserId: String,
//        ) = object : ViewModelProvider.Factory {
//            @Suppress("UNCHECKED_CAST")
//            override fun <T : ViewModel> create(modelClass: Class<T>): T {
//                if (modelClass.isAssignableFrom(MessageViewModel::class.java)) {
//                    return MessageViewModel(
//                        roomId = roomId,
//                        currentUserId = currentUserId,
//                    ) as T
//                }
//                throw IllegalArgumentException("Unknown ViewModel class")
//            }
//        }
////    }
//		return TODO("Provide the return value")
//	} }
