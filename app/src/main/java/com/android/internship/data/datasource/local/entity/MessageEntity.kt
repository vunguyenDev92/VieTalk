package com.android.internship.data.datasource.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.android.internship.data.model.Message

@Entity(tableName = "messages")
data class MessageEntity(
    @PrimaryKey
    val mid: String,
    val rid: String,
    val uid: String,
    val content: String,
    val time: String,
    val senderName: String,
    val senderAvatar: String?,
) {
    fun toMessage(): Message {
        return Message(
            mid = mid,
            rid = rid,
            uid = uid,
            content = content,
            time = time,
            senderName = senderName,
            senderAvatar = senderAvatar,
        )
    }

    companion object {
        fun fromMessage(message: Message): MessageEntity {
            return MessageEntity(
                mid = message.mid,
                rid = message.rid,
                uid = message.uid,
                content = message.content,
                time = message.time,
                senderName = message.senderName,
                senderAvatar = message.senderAvatar,
            )
        }
    }
}
