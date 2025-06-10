package com.android.internship.data.datasource.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "rooms")
data class RoomEntity(
    @PrimaryKey
    val rid: String,
    val isGroup: Boolean,
    val avatar: String?,
    val name: String?,
)
