package com.android.internship.data.datasource.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.android.internship.data.datasource.local.converter.ListStringConverter
import com.android.internship.data.datasource.local.converter.UserRoomConverter

@Entity(tableName = "messages")
@TypeConverters(ListStringConverter::class, UserRoomConverter::class)
data class MessageEntity(
    @PrimaryKey
    val mid: String,
    val rid: String,
    val uid: String,
    val content: String,
    val time: String,

)
