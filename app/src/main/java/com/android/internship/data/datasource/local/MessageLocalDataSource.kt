package com.android.internship.data.datasource.local

import android.content.Context
import com.android.internship.data.datasource.local.database.AppDatabase
import com.android.internship.data.datasource.local.entity.MessageEntity
import com.android.internship.data.model.Message

class MessageLocalDataSource(context: Context) {
    private val database = AppDatabase.getDatabase(context)
    private val messageDao = database.messageDao()

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
}
