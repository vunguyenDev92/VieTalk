package com.android.internship.data.datasource.local

import android.content.Context
import com.android.internship.data.datasource.local.database.AppDatabase
import com.android.internship.data.datasource.local.entity.MessageEntity

class MessageLocalDataSource(context: Context) {
    private val authLocalDataSource = AuthLocalDataSource(context)
    private val database = AppDatabase.getDatabase(context)
    private val messageDao = database.messageDao()

    suspend fun saveMessage(message: MessageEntity) {
        if (!authLocalDataSource.isUserSignedIn()) throw SecurityException("User is not signed in")
        val messageCount = messageDao.getMessageCountForRoom(message.rid)
        if (messageCount >= 20) {
            messageDao.deleteOldestMessage(message.rid)
        }
        messageDao.insertMessage(message)
    }

    suspend fun getLatestMessagesForRoom(rid: String): List<MessageEntity> {
        if (!authLocalDataSource.isUserSignedIn()) return emptyList()
        return messageDao.getLatestMessagesForRoom(rid).sortedBy { it.time }
    }

    suspend fun getOlderMessagesForRoom(rid: String, lastMessageTime: String): List<MessageEntity> {
        if (!authLocalDataSource.isUserSignedIn()) return emptyList()
        return messageDao.getOlderMessagesForRoom(rid, lastMessageTime).sortedBy { it.time }
    }
}
