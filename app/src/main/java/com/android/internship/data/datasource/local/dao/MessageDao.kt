package com.android.internship.data.datasource.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.android.internship.data.datasource.local.entity.MessageEntity

@Dao
interface MessageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: MessageEntity)

    @Query("SELECT * FROM messages WHERE rid = :rid ORDER BY time DESC LIMIT 20")
    suspend fun getLatestMessagesForRoom(rid: String): List<MessageEntity>

    @Query("SELECT * FROM messages WHERE rid = :rid AND time < :lastMessageTime ORDER BY time DESC LIMIT 20")
    suspend fun getOlderMessagesForRoom(rid: String, lastMessageTime: String): List<MessageEntity>
}
