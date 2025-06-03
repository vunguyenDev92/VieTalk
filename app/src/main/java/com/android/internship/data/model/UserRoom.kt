package com.android.internship.data.model

data class UserRoom(
    val rid: String,
    val uid: String,
    val mute: Boolean,
    val turnOnTime: String?,
    val lastSeenMessages: String?,
)
