package com.android.internship.domain.repository

import com.android.internship.data.model.UserRoom
import kotlinx.coroutines.flow.Flow

interface UserRoomRepository {
    // UserRoom Remote
    fun addUserRoomsRemote(userRooms: List<UserRoom>)
    suspend fun getUserRoomForUserRemote(uid: String): List<UserRoom>
    suspend fun getUserRoomsForRoomRemote(rid: String): List<UserRoom>
    fun updateTypingTime(rid: String, uid: String, time: String)
    fun updateMute(rid: String, uid: String, mute: Boolean, turnOnTime: String? = null)
    fun updateLastSeenMessages(rid: String, uid: String, lastSeenMessageId: String? = null)
    fun observeUserRoomDetails(rid: String): Flow<List<UserRoom>>

    // UserRoom Local
    suspend fun getUserRoomForUserLocal(rid: String): List<UserRoom>?
    suspend fun getUserRoomForRoomLocal(rid: String): List<UserRoom>?
    suspend fun saveLocalUserRoom(userRooms: List<UserRoom>)
}
