package com.android.internship.data.model

data class Message(
    val mid: String = "",
    val rid: String = "",
    val uid: String = "",
    val senderName: String = "",
    val senderAvatar: String? = null,
    val content: String = "",
    val time: String = "",
)
