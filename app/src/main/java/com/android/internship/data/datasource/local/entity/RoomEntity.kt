package com.android.internship.data.datasource.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.android.internship.data.model.Room

@Entity(tableName = "rooms")
data class RoomEntity(
    @PrimaryKey
    val rid: String,
    val isGroup: Boolean,
    val avatar: String?,
    val name: String?,
    val listUser: List<String>,
    val lastMessage: MessageEntity,
    val updatedAt: String,
) {
    fun toRoom(): Room {
        return Room(
            rid = rid,
            isGroup = isGroup,
            avatar = avatar,
            name = name,
            listUser = listUser,
            lastMessage = lastMessage.toMessage(),
            updatedAt = updatedAt,
        )
    }

    companion object {
        fun fromRoom(room: Room): RoomEntity {
            return RoomEntity(
                rid = room.rid,
                isGroup = room.isGroup,
                avatar = room.avatar,
                name = room.name,
                listUser = room.listUser,
                lastMessage = MessageEntity(
                    mid = room.lastMessage.mid,
                    rid = room.lastMessage.rid,
                    uid = room.lastMessage.uid,
                    content = room.lastMessage.content,
                    time = room.lastMessage.time,
                    senderName = room.lastMessage.senderName,
                    senderAvatar = room.lastMessage.senderAvatar,
                ),
                updatedAt = room.updatedAt,
            )
        }
    }
}
