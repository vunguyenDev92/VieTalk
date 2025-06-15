package com.android.internship.data.datasource.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.android.internship.data.model.User

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey
    val uid: String,
    val username: String,
    val lastActiveTime: String,
    val avatar: String?,
) {
    fun toUser(): User {
        return User(
            uid = uid,
            username = username,
            lastActiveTime = lastActiveTime,
            avatar = avatar,
        )
    }

    companion object {
        fun fromUser(user: User): UserEntity {
            return UserEntity(
                uid = user.uid,
                username = user.username,
                lastActiveTime = user.lastActiveTime,
                avatar = user.avatar,
            )
        }
    }
}
