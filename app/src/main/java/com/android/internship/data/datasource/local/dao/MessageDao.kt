package com.android.internship.data.datasource.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.android.internship.data.datasource.local.entity.MessageEntity

@Dao
interface MessageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessages(messages: List<MessageEntity>)

    @Query("SELECT * FROM messages WHERE rid = :rid")
    suspend fun getMessage(rid: String): List<MessageEntity>

    @Query("SELECT * FROM messages WHERE rid = :rid ORDER BY time DESC LIMIT :limit")
    suspend fun getLatestMessagesForRoom(rid: String, limit: Int): List<MessageEntity>

    @Query("SELECT * FROM messages WHERE rid = :rid AND mid < :startMessageId ORDER BY time DESC LIMIT :limit")
    suspend fun getOlderMessagesForRoom(rid: String, startMessageId: String, limit: Int): List<MessageEntity>

    @Query("SELECT COUNT(*) FROM messages WHERE rid = :rid")
    suspend fun getMessageCountForRoom(rid: String): Int

    @Query("DELETE FROM messages WHERE rid = :rid")
    suspend fun clearMessagesForRoom(rid: String)
}
