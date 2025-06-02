package com.android.internship.data.model

data class Room(
    val rid: String,
    val isGroup: Boolean,
    val users: List<UserRoom>,
    val messages: List<Message>,
    val isTyping: List<String>,
    val avatar: String?,
    val name: String?,
)
