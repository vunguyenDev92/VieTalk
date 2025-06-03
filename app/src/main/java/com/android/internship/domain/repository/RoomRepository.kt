package com.android.internship.domain.repository

import com.android.internship.data.model.Message
import com.android.internship.data.model.Room
import com.android.internship.data.model.UserRoom

interface RoomRepository {
    suspend fun getRoomLocal(rid: String): Room
    suspend fun getRoomRemote(rid: String): Room
    suspend fun addMessage(rid: String, message: Message): Boolean
    suspend fun addTyping(rid: String, uid: String): Boolean
    suspend fun removeTyping(rid: String, uid: String): Boolean
    suspend fun seenMessage(rid: String, uid: String, mid: String)
    suspend fun getTypingUsersInRoom(rid: String): List<UserRoom>
}
