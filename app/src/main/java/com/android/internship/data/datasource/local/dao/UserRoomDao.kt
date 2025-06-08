package com.android.internship.data.datasource.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.android.internship.data.datasource.local.entity.UserRoomEntity

@Dao
interface UserRoomDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserRooms(userRooms: List<UserRoomEntity>)

    @Query("SELECT * FROM userRooms WHERE rid = :rid")
    suspend fun getUserRoomsForRoom(rid: String): List<UserRoomEntity>

    @Query("SELECT * FROM userRooms WHERE rid = :rid AND uid = :uid")
    suspend fun getUserRoom(rid: String, uid: String): UserRoomEntity?

    @Query("UPDATE userRooms SET lastSeenMessages = :lastSeenMessages WHERE rid = :rid AND uid = :uid")
    suspend fun updateLastSeenMessages(rid: String, uid: String, lastSeenMessages: String?)

    @Query("UPDATE userRooms SET typingTime = :typingTime WHERE rid = :rid AND uid = :uid")
    suspend fun updateTypingStatus(rid: String, uid: String, typingTime: String?)
}
