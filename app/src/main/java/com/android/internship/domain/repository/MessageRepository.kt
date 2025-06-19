package com.android.internship.domain.repository

import com.android.internship.data.model.Message
import kotlinx.coroutines.flow.Flow

interface MessageRepository {
    // Remote message operations
    fun addRemoteMessageNew(mid: String, rid: String, uid: String, content: String, timestamp: String, senderAvt: String, senderName: String)

//    fun addRemoteMessage(mid: String, rid: String, uid: String, content: String, timestamp: String)
    suspend fun getRemoteMessages(rid: String, startMessageId: String?, limit: Int): List<Message>?
    fun observeMessages(rid: String): Flow<List<Message>>

    // Local message operations
    suspend fun getLocalMessages(rid: String): List<Message>?
    suspend fun saveLocalMessages(messages: List<Message>)
    fun observeNewMessages(roomId: String, afterTimestamp: Long): Flow<List<Message>>

    // merge
    suspend fun syncRemoteMessagesToLocal(rid: String)
    suspend fun fetchAndCacheInitialMessages(rid: String, limit: Int)

    suspend fun fetchAndCacheOlderMessages(rid: String, limit: Int): Boolean

    suspend fun getLatestLocalMessage(rid: String): Message?
}
