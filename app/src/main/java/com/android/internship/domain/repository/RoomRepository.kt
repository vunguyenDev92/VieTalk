package com.android.internship.domain.repository

import com.android.internship.data.model.Message
import com.android.internship.data.model.Room
import com.android.internship.data.model.UserRoom

interface RoomRepository {
    // Room Remote
    suspend fun getRoomRemote(rid: String): Room?
    suspend fun getUserRoomRemote(rid: String): List<UserRoom>

    // Typing
    fun addTyping(rid: String, uid: String, time: String)

    // Message
    fun addRemoteMessage(rid: String, message: Message)
    fun seenMessage(rid: String, uid: String, mid: String)
    suspend fun getRemoteMessages(rid: String, startMessageId: String?, limit: Int): List<Message>?
    suspend fun getLocalMessages(rid: String): List<Message>?
    suspend fun saveLocalMessages(messages: List<Message>)

    // Room Local
    suspend fun getRoomLocal(rid: String): Room?
    suspend fun saveLocalRoom(room: Room)
    suspend fun getUserRoomLocal(rid: String): List<UserRoom>?
    suspend fun saveLocalUserRoom(userRooms: List<UserRoom>)
}
