package com.android.internship.data.repository

import com.android.internship.data.datasource.local.RoomLocalDataSource
import com.android.internship.data.datasource.remote.RoomRemoteDataSource
import com.android.internship.data.model.Message
import com.android.internship.data.model.Room
import com.android.internship.domain.repository.RoomRepository
import kotlinx.coroutines.flow.Flow

class RoomRepositoryImpl(
    private val roomLocalDataSource: RoomLocalDataSource,
    private val roomRemoteDataSource: RoomRemoteDataSource,
) : RoomRepository {

    override fun addRoomRemote(room: Room) {
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

    override fun observeRooms(): Flow<List<Room>> {
        return roomRemoteDataSource.observeRooms()
    }

    override fun observeRoom(rid: String): Flow<Room?> {
        return roomRemoteDataSource.observeRoom(rid)
    }

    override fun updateLastMessage(rid: String, message: Message) {
        roomRemoteDataSource.updateLastMessage(rid, message)
    }
}
