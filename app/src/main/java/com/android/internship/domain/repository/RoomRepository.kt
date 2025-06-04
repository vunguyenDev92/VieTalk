package com.android.internship.domain.repository

import com.android.internship.data.model.Message
import com.android.internship.data.model.Room

interface RoomRepository {
    suspend fun getRoomRemote(rid: String): Room?
    suspend fun addRemoteMessage(rid: String, message: Message): Boolean
    suspend fun addTyping(rid: String, uid: String): Boolean
    suspend fun removeTyping(rid: String, uid: String): Boolean
    suspend fun seenMessage(rid: String, uid: String, mid: String)
    suspend fun getTypingUsersInRoom(rid: String): List<String>
    suspend fun getLatestMessagesForRoom(rid: String): Message
    suspend fun getMessagesForRoom(rid: String, startMessageId: String, limit: Int): List<Message>

    suspend fun getRoomLocal(rid: String): Room?
    suspend fun saveLocalRoom(room: Room)
}
