package com.android.internship.data.datasource.local.entity

import androidx.room.Entity
import com.android.internship.data.model.UserRoom

@Entity(tableName = "userRooms", primaryKeys = ["rid", "uid"])
data class UserRoomEntity(
    val rid: String,
    val uid: String,
    val mute: Boolean,
    val turnOnTime: String?,
    val lastSeenMessages: String?,
    val typingTime: String?,
    val isBlocked: Boolean,
) {
    fun toUserRoom(): UserRoom {
        return UserRoom(
            rid = rid,
            uid = uid,
            mute = mute,
            turnOnTime = turnOnTime,
            lastSeenMessages = lastSeenMessages,
            typingTime = typingTime,
            isBlocked = isBlocked,
        )
    }

    companion object {
        fun fromUserRoom(userRoom: UserRoom): UserRoomEntity {
            return UserRoomEntity(
                rid = userRoom.rid,
                uid = userRoom.uid,
                mute = userRoom.mute,
                turnOnTime = userRoom.turnOnTime,
                lastSeenMessages = userRoom.lastSeenMessages,
                typingTime = userRoom.typingTime,
                isBlocked = userRoom.isBlocked,
            )
        }
    }
}
