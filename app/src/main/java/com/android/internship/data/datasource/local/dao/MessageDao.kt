package com.android.internship.data.datasource.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.android.internship.data.datasource.local.entity.MessageEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MessageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessages(messages: List<MessageEntity>)

    @Query("SELECT * FROM messages WHERE rid = :rid")
    suspend fun getMessages(rid: String): List<MessageEntity>

    // old to new
    @Query("SELECT * FROM messages WHERE rid = :rid ORDER BY time ASC")
    fun observeMessages(rid: String): Flow<List<MessageEntity>>

    @Query("SELECT COUNT(mid) FROM messages WHERE rid = :rid")
    suspend fun countMessagesInRoom(rid: String): Int

    @Query("SELECT mid FROM messages WHERE rid = :rid ORDER BY time ASC LIMIT :limit")
    suspend fun getOldestMessageIds(rid: String, limit: Int): List<String>

    @Query("DELETE FROM messages WHERE mid IN (:messageIds)")
    suspend fun deleteMessagesByIds(messageIds: List<String>)

    @Transaction
    suspend fun insertAndTrimMessages(rid: String, messagesToInsert: List<MessageEntity>, limit: Int) {
        insertMessages(messagesToInsert)

        val currentCount = countMessagesInRoom(rid)

        if (currentCount > limit) {
            val amountToDelete = currentCount - limit
            val idsToDelete = getOldestMessageIds(rid, amountToDelete)
            if (idsToDelete.isNotEmpty()) {
                deleteMessagesByIds(idsToDelete)
            }
        }
    }

    // oldest mess to be cursor
    @Query("SELECT * FROM messages WHERE rid = :rid ORDER BY time ASC LIMIT 1")
    suspend fun getOldestMessage(rid: String): MessageEntity?

    // newest mess to listen from remote
    @Query("SELECT * FROM messages WHERE rid = :rid ORDER BY time DESC LIMIT 1")
    suspend fun getLatestMessage(rid: String): MessageEntity?
}
