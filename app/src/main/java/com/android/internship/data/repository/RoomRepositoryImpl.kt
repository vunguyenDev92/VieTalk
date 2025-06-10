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

    override suspend fun getRoomRemote(rid: String): Room? {
        return roomRemoteDataSource.getRoomById(rid)
    }

    override suspend fun getRoomLocal(rid: String): Room? {
        return roomLocalDataSource.getRoomById(rid)
    }

    override suspend fun saveLocalRoom(room: Room) {
        roomLocalDataSource.saveRoomLocal(room)
    }
}
