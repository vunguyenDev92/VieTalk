package com.android.internship.data.datasource.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.android.internship.data.datasource.local.entity.UserEntity

@Dao
interface UserDao {
    @Query("SELECT * FROM users WHERE isCurrentUser = 1 LIMIT 1")
    suspend fun getCurrentUser(): UserEntity?

    @Query("SELECT uid FROM users WHERE isCurrentUser = 1 LIMIT 1")
    suspend fun getCurrentUserId(): String?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveUser(user: UserEntity)

    @Query("UPDATE users SET isCurrentUser = 0")
    suspend fun clearCurrentUserFlag()

    @Query("DELETE FROM users WHERE isCurrentUser = 1")
    suspend fun deleteCurrentUser()

    @Query("SELECT COUNT(*) > 0 FROM users WHERE isCurrentUser = 1")
    suspend fun hasCurrentUser(): Boolean
}
