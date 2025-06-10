package com.android.internship.data.datasource.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.android.internship.data.datasource.local.entity.BlockEntity

@Dao
interface BlockDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBlock(block: BlockEntity)

    @Query("SELECT * FROM blocks WHERE uid = :uid AND blockedUid = :blockedUid")
    suspend fun getBlock(uid: String, blockedUid: String): BlockEntity?

    @Query("DELETE FROM blocks WHERE uid = :uid AND blockedUid = :blockedUid")
    suspend fun deleteBlock(uid: String, blockedUid: String)
}
