package com.android.internship.data.datasource.local

import android.content.Context
import com.android.internship.data.datasource.local.database.AppDatabase
import com.android.internship.data.datasource.local.entity.RoomEntity
import com.android.internship.data.model.Room

class RoomLocalDataSource(context: Context) {
    private val database = AppDatabase.getDatabase(context)
    private val roomDao = database.roomDao()

    suspend fun saveRoomLocal(room: Room) {
        roomDao.insertRoom(
            RoomEntity(
                rid = room.rid,
                isGroup = room.isGroup,
                avatar = room.avatar,
                name = room.name,
            ),
        )
    }

    suspend fun getRoomById(rid: String): Room? {
        val roomEntity = roomDao.getRoomById(rid)
        return roomEntity?.let {
            Room(
                rid = it.rid,
                isGroup = it.isGroup,
                avatar = it.avatar,
                name = it.name,
            )
        }
    }

    suspend fun getAllRoom(): List<Room>? {
        val roomEntities = roomDao.getAllRooms()
        return roomEntities.map {
            Room(
                rid = it.rid,
                isGroup = it.isGroup,
                avatar = it.avatar,
                name = it.name,
            )
        }
    }

    suspend fun insertRoom(userRoom: Room) {
        roomDao.insertRoom(
            RoomEntity(
                rid = userRoom.rid,
                isGroup = userRoom.isGroup,
                avatar = userRoom.avatar,
                name = userRoom.name,
            ),
        )
    }
}
