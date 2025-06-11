package com.android.internship.data.datasource.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.android.internship.data.datasource.local.entity.RoomEntity

@Dao
interface RoomDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRoom(room: RoomEntity)

    @Query("SELECT * FROM rooms WHERE rid = :rid")
    suspend fun getRoomById(rid: String): RoomEntity?

    @Query("SELECT * FROM rooms")
    suspend fun getAllRooms(): List<RoomEntity>
}
