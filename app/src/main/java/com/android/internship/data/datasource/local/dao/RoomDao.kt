package com.android.internship.data.datasource.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.android.internship.data.datasource.local.entity.RoomEntity
import com.android.internship.data.datasource.local.entity.UserRoomEntity

@Dao
interface RoomDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRoom(room: RoomEntity)

    @Query("SELECT * FROM rooms WHERE rid = :rid")
    suspend fun getRoomById(rid: String): RoomEntity?

    @Query("SELECT * FROM rooms")
    suspend fun getAllRooms(): List<RoomEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserRoom(userRoom: UserRoomEntity)

    @Query("SELECT * FROM user_rooms WHERE rid = :rid")
    suspend fun getUserRoomsForRoom(rid: String): List<UserRoomEntity>

    @Query("SELECT * FROM user_rooms WHERE rid = :rid AND uid = :uid")
    suspend fun getUserRoom(rid: String, uid: String): UserRoomEntity?

    @Query("UPDATE user_rooms SET lastSeenMessages = :lastSeenMessages WHERE rid = :rid AND uid = :uid")
    suspend fun updateLastSeenMessages(rid: String, uid: String, lastSeenMessages: String?)

    @Query("UPDATE user_rooms SET isTyping = :isTyping WHERE rid = :rid AND uid = :uid")
    suspend fun updateIsTyping(rid: String, uid: String, isTyping: Boolean)

    @Query("SELECT * FROM user_rooms WHERE rid = :rid AND isTyping = 1")
    suspend fun getTypingUsersInRoom(rid: String): List<UserRoomEntity>
}
