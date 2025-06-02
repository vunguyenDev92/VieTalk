package com.android.internship.data.datasource.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.android.internship.data.datasource.local.converter.ListStringConverter
import com.android.internship.data.datasource.local.converter.UserRoomConverter
import com.android.internship.data.model.UserRoom

@Entity(tableName = "users")
@TypeConverters(ListStringConverter::class, UserRoomConverter::class)
data class UserEntity(
    @PrimaryKey
    val uid: String,
    val username: String,
    val email: String,
    val active: Boolean,
    val avatar: String?,
    val block: List<String>?,
    val userRooms: List<UserRoom>?,
    val isCurrentUser: Boolean = false,
)
