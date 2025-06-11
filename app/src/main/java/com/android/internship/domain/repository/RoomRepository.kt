package com.android.internship.domain.repository

import com.android.internship.data.model.Room

interface RoomRepository {
    // Room Remote
    fun addRoomRemote(rid: String, isGroup: Boolean, avatar: String? = null, name: String? = null)
    suspend fun getRoomRemote(rid: String): Room?

    // Room Local
    suspend fun getRoomLocal(rid: String): Room?
    suspend fun saveLocalRoom(room: Room)
}
