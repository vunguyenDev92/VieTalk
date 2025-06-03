package com.android.internship.data.datasource.local

import android.content.Context
import com.android.internship.data.datasource.local.database.AppDatabase
import com.android.internship.data.datasource.local.entity.RoomEntity
import com.android.internship.data.datasource.local.entity.UserRoomEntity

class RoomLocalDataSource(context: Context) {
    private val authLocalDataSource = AuthLocalDataSource(context)
    private val database = AppDatabase.getDatabase(context)
    private val roomDao = database.roomDao()

    suspend fun saveRoom(room: RoomEntity) {
        if (!authLocalDataSource.isUserSignedIn()) throw SecurityException("User is not signed in")
        roomDao.insertRoom(room)
    }

    suspend fun getRoomById(rid: String): RoomEntity? {
        if (!authLocalDataSource.isUserSignedIn()) return null
        return roomDao.getRoomById(rid)
    }

    suspend fun getAllRoom(): List<RoomEntity>? {
        if (!authLocalDataSource.isUserSignedIn()) return null
        return roomDao.getAllRooms()
    }

    suspend fun insertUserRoom(userRoom: UserRoomEntity) {
        if (!authLocalDataSource.isUserSignedIn()) throw SecurityException("User is not signed in")
        roomDao.insertUserRoom(userRoom)
    }

    suspend fun getUserRoomForRoom(rid: String): List<UserRoomEntity> {
        if (!authLocalDataSource.isUserSignedIn()) return emptyList()
        return roomDao.getUserRoomsForRoom(rid)
    }

    suspend fun getUserRoom(rid: String, uid: String): UserRoomEntity? {
        if (!authLocalDataSource.isUserSignedIn()) return null
        return roomDao.getUserRoom(rid, uid)
    }

    suspend fun updateIsTyping(rid: String, uid: String, isTyping: Boolean) {
        if (!authLocalDataSource.isUserSignedIn()) throw SecurityException("User is not signed in")
        roomDao.updateIsTyping(rid, uid, isTyping)
    }

    suspend fun updateLastSeenMessages(rid: String, uid: String, lastSeenMessages: String?) {
        if (!authLocalDataSource.isUserSignedIn()) throw SecurityException("User is not signed in")
        roomDao.updateLastSeenMessages(rid, uid, lastSeenMessages)
    }

    suspend fun getTypingUsersInRoom(rid: String): List<UserRoomEntity> {
        if (!authLocalDataSource.isUserSignedIn()) return emptyList()
        return roomDao.getTypingUsersInRoom(rid)
    }
}
