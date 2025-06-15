package com.android.internship.data.repository

import com.android.internship.data.datasource.local.RoomLocalDataSource
import com.android.internship.data.datasource.remote.RoomRemoteDataSource
import com.android.internship.data.model.Room
import com.android.internship.domain.repository.RoomRepository

class RoomRepositoryImpl(
    private val roomLocalDataSource: RoomLocalDataSource,
    private val roomRemoteDataSource: RoomRemoteDataSource,
) : RoomRepository {

    override fun addRoomRemote(
        rid: String,
        isGroup: Boolean,
        avatar: String?,
        name: String?,
    ) {
        val room = Room(rid = rid, isGroup = isGroup, avatar = avatar, name = name)
        roomRemoteDataSource.addRoom(room)
    }

    override suspend fun getRoomsRemote(rids: List<String>): List<Room>? {
        return roomRemoteDataSource.getRooms(rids)
    }

    override suspend fun getRoomsLocal(rids: List<String>): List<Room>? {
        return roomLocalDataSource.getRoomsLocal(rids)
    }

    override suspend fun saveRoomsLocal(rooms: List<Room>) {
        roomLocalDataSource.saveRoomsLocal(rooms)
    }
}
