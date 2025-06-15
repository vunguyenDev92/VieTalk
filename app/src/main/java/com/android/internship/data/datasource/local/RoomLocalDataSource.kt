package com.android.internship.data.datasource.local

import android.content.Context
import com.android.internship.data.datasource.local.database.AppDatabase
import com.android.internship.data.datasource.local.entity.RoomEntity
import com.android.internship.data.model.Room

class RoomLocalDataSource(context: Context) {
    private val database = AppDatabase.getDatabase(context)
    private val roomDao = database.roomDao()

    suspend fun saveRoomsLocal(rooms: List<Room>) {
        roomDao.insertRooms(
            rooms.map { room ->
                RoomEntity.fromRoom(room)
            },
        )
    }

    suspend fun getRoomsLocal(rids: List<String>): List<Room> {
        return roomDao.getRoomsByIds(rids).map { roomEntity ->
            roomEntity.toRoom()
        }
    }
}
