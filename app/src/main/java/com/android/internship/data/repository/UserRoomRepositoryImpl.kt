package com.android.internship.data.repository

import com.android.internship.data.datasource.local.UserRoomLocalDataSource
import com.android.internship.data.datasource.remote.UserRoomRemoteDataSource
import com.android.internship.data.model.UserRoom
import com.android.internship.domain.repository.UserRoomRepository
import kotlinx.coroutines.flow.Flow

class UserRoomRepositoryImpl(
    private val userRoomLocalDataSource: UserRoomLocalDataSource,
    private val userRoomRemoteDataSource: UserRoomRemoteDataSource,
) : UserRoomRepository {

    override fun addUserRoomsRemote(userRooms: List<UserRoom>) {
        userRoomRemoteDataSource.addUserRoomRemote(userRooms)
    }

    override fun observeUserRoomForUserRemote(uid: String): Flow<List<UserRoom>> {
        return userRoomRemoteDataSource.getUserRoomsForUser(uid)
    }

    override suspend fun getUserRoomsForRoomRemote(rid: String): List<UserRoom> {
        return userRoomRemoteDataSource.getUserRoomsForRoom(rid)
    }

    override fun updateMute(
        rid: String,
        uid: String,
        mute: Boolean,
        turnOnTime: String?,
    ) {
        userRoomRemoteDataSource.updateMute(rid, uid, mute, turnOnTime)
    }

    override fun updateBlock(rid: String, uid: String, isBlocked: Boolean) {
        userRoomRemoteDataSource.updateBlock(rid, uid, isBlocked)
    }

    override fun updateLastSeenMessages(
        rid: String,
        uid: String,
        lastSeenMessagesId: String?,
    ) {
        userRoomRemoteDataSource.updateLastSeenMessages(rid, uid, lastSeenMessagesId)
    }

    override fun updateTypingTime(rid: String, uid: String, time: String) {
        userRoomRemoteDataSource.updateTypingTime(rid, uid, time)
    }

    override fun observeUserRoomDetails(rid: String): Flow<List<UserRoom>> {
        return userRoomRemoteDataSource.observeUserRoomsForRoom(rid)
    }

    override suspend fun getUserRoomForUserLocal(rid: String): List<UserRoom>? {
        return userRoomLocalDataSource.getUserRoomsForUser(rid)
    }

    override suspend fun getUserRoomForRoomLocal(rid: String): List<UserRoom>? {
        return userRoomLocalDataSource.getUserRoomsForRoom(rid)
    }

    override suspend fun saveLocalUserRoom(userRooms: List<UserRoom>) {
        userRoomLocalDataSource.insertUserRooms(userRooms)
    }
}
