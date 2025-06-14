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

    @Query("SELECT * FROM userRooms WHERE uid = :uid")
    suspend fun getUserRoomsForUser(uid: String): List<UserRoomEntity>
}
