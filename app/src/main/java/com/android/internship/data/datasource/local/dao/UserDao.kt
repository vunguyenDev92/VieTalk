package com.android.internship.data.datasource.local.dao

import androidx.room.Dao
import androidx.room.Query
import com.android.internship.data.datasource.local.entity.UserEntity

@Dao
interface UserDao {
    @Query("SELECT * FROM users WHERE isCurrentUser = 1 LIMIT 1")
    suspend fun getCurrentUser(): UserEntity?

    @Query("SELECT uid FROM users WHERE isCurrentUser = 1 LIMIT 1")
    suspend fun getCurrentUserId(): String?
}
