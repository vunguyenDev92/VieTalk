package com.android.internship.data.datasource.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "messages")
data class MessageEntity(
    @PrimaryKey
    val mid: String,
    val rid: String,
    val uid: String,
    val content: String,
    val time: String,
)
