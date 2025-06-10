package com.android.internship.data.datasource.local

import android.content.Context
import com.android.internship.data.datasource.local.database.AppDatabase
import com.android.internship.data.datasource.local.entity.MessageEntity
import com.android.internship.data.model.Message

class MessageLocalDataSource(context: Context) {
    private val database = AppDatabase.getDatabase(context)
    private val messageDao = database.messageDao()

    suspend fun saveLocalMessage(messages: List<Message>) {
        val messageEntities = messages.map { message ->
            MessageEntity(
                mid = message.mid,
                rid = message.rid,
                uid = message.uid,
                content = message.content,
                time = message.time,
            )
        }
        messageDao.insertMessages(messageEntities)
    }

    suspend fun getMessage(rid: String): List<Message> {
        return messageDao.getMessage(rid).map {
            Message(
                mid = it.mid,
                rid = it.rid,
                uid = it.uid,
                content = it.content,
                time = it.time,
            )
        }
    }
}
