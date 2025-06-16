package com.android.internship.data.repository

import com.android.internship.data.datasource.local.MessageLocalDataSource
import com.android.internship.data.datasource.remote.MessageRemoteDataSource
import com.android.internship.data.model.Message
import com.android.internship.domain.repository.MessageRepository
import kotlinx.coroutines.flow.Flow

class MessageRepositoryImpl(
    private val messageLocalDataSource: MessageLocalDataSource,
    private val messageRemoteDataSource: MessageRemoteDataSource,
) : MessageRepository {

    override fun observeMessages(rid: String): Flow<List<Message>> {
        return messageRemoteDataSource.observeMessages(rid)
    }

    override fun addRemoteMessageNew(
        mid: String,
        rid: String,
        uid: String,
        content: String,
        timestamp: String,
        senderAvt: String,
        senderName: String,
    ) {
        messageRemoteDataSource.addRemoteMessage(
            Message(
                mid = mid,
                rid = rid,
                uid = uid,
                content = content,
                time = timestamp,
                senderAvatar = senderAvt,
                senderName = senderName,
            ),
        )
    }
//
//    override fun addRemoteMessage(
//        mid: String,
//        rid: String,
//        uid: String,
//        content: String,
//        timestamp: String,
//    ) {
//        messageRemoteDataSource.addRemoteMessage(
//            Message(
//                mid = mid,
//                rid = rid,
//                uid = uid,
//                content = content,
//                time = timestamp,
//            ),
//        )
//    }
//

    override suspend fun getRemoteMessages(
        rid: String,
        startMessageId: String?,
        limit: Int,
    ): List<Message>? {
        return messageRemoteDataSource.getRemoteMessages(rid, startMessageId, limit)
    }

    override suspend fun getLocalMessages(rid: String): List<Message>? {
        return messageLocalDataSource.getMessages(rid)
    }

    override suspend fun saveLocalMessages(messages: List<Message>) {
        return messageLocalDataSource.saveLocalMessage(messages)
    }

    override fun observeNewMessages(
        roomId: String,
        afterTimestamp: Long,
    ): Flow<List<Message>> {
        return messageRemoteDataSource.observeNewMessages(roomId, afterTimestamp)
    }

    override suspend fun syncRemoteMessagesToLocal(rid: String) {
        val lastLocalMessageTime = messageLocalDataSource.getMessages(rid)
            .lastOrNull()?.time?.toLongOrNull() ?: 0L

        messageRemoteDataSource.observeNewMessages(rid, lastLocalMessageTime).collect { remoteMessages ->
            if (remoteMessages.isNotEmpty()) {
                val currentLocalMessages = messageLocalDataSource.getMessages(rid)
                val allMessages = (currentLocalMessages + remoteMessages).distinctBy { it.mid }
                messageLocalDataSource.saveAndTrimLocalMessages(rid, allMessages)
            }
        }
    }

    override suspend fun fetchAndCacheInitialMessages(rid: String, limit: Int) {
        val remoteMessages = messageRemoteDataSource.getRemoteMessages(rid, null, limit)
        remoteMessages?.let {
            if (it.isNotEmpty()) {
                messageLocalDataSource.saveAndTrimLocalMessages(rid, it)
            }
        }
    }
}
