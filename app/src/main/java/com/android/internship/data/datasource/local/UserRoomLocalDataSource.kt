package com.android.internship.data.datasource.local

import android.content.Context
import com.android.internship.data.datasource.local.database.AppDatabase
import com.android.internship.data.datasource.local.entity.UserRoomEntity
import com.android.internship.data.model.UserRoom

class UserRoomLocalDataSource(context: Context) {
    private val database = AppDatabase.getDatabase(context)
    private val userRoomDao = database.userRoomDao()

    suspend fun insertUserRooms(userRooms: List<UserRoom>) {
        userRoomDao.insertUserRooms(
            userRooms.map {
                UserRoomEntity.fromUserRoom(it)
            },
        )
    }

    suspend fun getUserRoomsForRoom(rid: String): List<UserRoom> {
        return userRoomDao.getUserRoomsForRoom(rid).map {
            it.toUserRoom()
        }
    }

    suspend fun getUserRoomsForUser(uid: String): List<UserRoom> {
        return userRoomDao.getUserRoomsForUser(uid).map {
            it.toUserRoom()
        }
    }
}
