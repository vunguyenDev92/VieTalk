package com.android.internship.data.datasource.local

import android.content.Context
import com.android.internship.data.datasource.local.database.AppDatabase
import com.android.internship.data.datasource.local.entity.MessageEntity
import com.android.internship.data.model.Message
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MessageLocalDataSource(context: Context) {
    private val database = AppDatabase.getDatabase(context)
    private val messageDao = database.messageDao()

    companion object {
        const val MESSAGE_CACHE_LIMIT = 20
    }

    suspend fun saveLocalMessage(messages: List<Message>) {
        messageDao.insertMessages(
            messages.map { message ->
                MessageEntity.fromMessage(message)
            },
        )
    }

    suspend fun getMessages(rid: String): List<Message> {
        return messageDao.getMessages(rid).map {
            it.toMessage()
        }
    }

    fun observeMessages(rid: String): Flow<List<Message>> {
        return messageDao.observeMessages(rid).map { entities ->
            entities.map { it.toMessage() }
        }
    }

    suspend fun saveAndTrimLocalMessages(rid: String, messages: List<Message>) {
        messageDao.insertAndTrimMessages(
            rid = rid,
            messagesToInsert = messages.map { MessageEntity.fromMessage(it) },
            limit = MESSAGE_CACHE_LIMIT,
        )
    }
}
