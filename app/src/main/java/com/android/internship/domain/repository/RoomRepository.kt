package com.android.internship.domain.repository

import com.android.internship.data.model.Room

interface RoomRepository {
    // Room Remote
    fun addRoomRemote(rid: String, isGroup: Boolean, avatar: String? = null, name: String? = null)
    suspend fun getRoomsRemote(rids: List<String>): List<Room>?

    // Room Local
    suspend fun getRoomsLocal(rids: List<String>): List<Room>?
    suspend fun saveRoomsLocal(rooms: List<Room>)
}
