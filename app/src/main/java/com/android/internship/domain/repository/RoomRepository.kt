package com.android.internship.domain.repository

import com.android.internship.data.model.Message
import com.android.internship.data.model.Room

interface RoomRepository {
    // Room Remote
    suspend fun getRoomRemote(rid: String): Room?

    // Typing
    fun addTyping(rid: String, uid: String)
    fun removeTyping(rid: String, uid: String)
    suspend fun getTypingUsers(rid: String): List<String>

    // Message
    fun addRemoteMessage(rid: String, message: Message)
    fun seenMessage(rid: String, uid: String, mid: String)
    suspend fun getRemoteMessages(rid: String, startMessageId: String?, limit: Int): List<Message>?
    suspend fun getLocalMessages(rid: String): List<Message>?

    // Room Local
    suspend fun getRoomLocal(rid: String): Room?
    suspend fun saveLocalRoom(room: Room)
}
