package com.android.internship.domain.repository

import com.android.internship.data.model.Message
import com.android.internship.data.model.Room
import kotlinx.coroutines.flow.Flow

interface RoomRepository {
    // Room Remote
    fun addRoomRemote(room: Room)
    suspend fun getRoomsRemote(rids: List<String>): List<Room>?
    fun updateLastMessage(rid: String, message: Message)

    // Room Local
    suspend fun getRoomsLocal(rids: List<String>): List<Room>?
    suspend fun saveRoomsLocal(rooms: List<Room>)
    fun observeRooms(): Flow<List<Room>>
}
