package com.android.internship.data.datasource.local.entity

import androidx.room.Entity

@Entity(tableName = "user_rooms", primaryKeys = ["rid", "uid"])
data class UserRoomEntity(
    val rid: String,
    val uid: String,
    val mute: Boolean,
    val turnOnTime: String?,
    val lastSeenMessages: String?,
    val isTyping: Boolean = false,
)
