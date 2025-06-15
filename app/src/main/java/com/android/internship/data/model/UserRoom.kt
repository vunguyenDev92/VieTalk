package com.android.internship.data.model

data class UserRoom(
    val rid: String = "",
    val uid: String = "",
    val mute: Boolean = false,
    val turnOnTime: String? = null,
    val lastSeenMessages: String? = null,
    val typingTime: String? = null,
    val isBlocked: Boolean = false,
)
