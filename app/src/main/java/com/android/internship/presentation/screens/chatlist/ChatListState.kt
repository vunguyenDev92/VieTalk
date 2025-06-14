package com.android.internship.presentation.screens.chatlist

data class ChatListState(
    val chatRoomItems: List<ChatRoomItemState> = emptyList(),
    val chatUserItems: List<ChatUserItemState> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
) {
    data class ChatRoomItemState(
        val isGroupChat: Boolean = false,
        val isActive: Boolean = false,
        val id: String = "",
        val name: String = "",
        val memberAvatars: List<String>? = null,
        val lastMessage: String = "",
        val lastMessageTime: String = "",
        val lastSenderName: String = "",
    )

    data class ChatUserItemState(
        val isOnline: Boolean = false,
        val id: String = "",
        val name: String = "",
        val avatar: String? = null,
    )
}
