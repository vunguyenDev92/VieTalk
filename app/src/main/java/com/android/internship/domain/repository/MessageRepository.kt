package com.android.internship.domain.repository

import com.android.internship.data.model.Message
import kotlinx.coroutines.flow.Flow

interface MessageRepository {
    // Remote message operations
    fun addRemoteMessage(mid: String, rid: String, uid: String, content: String, timestamp: String)
    suspend fun getRemoteMessages(rid: String, startMessageId: String?, limit: Int): List<Message>?
    fun observeMessages(rid: String): Flow<List<Message>>

    // Local message operations
    suspend fun getLocalMessages(rid: String): List<Message>?
    suspend fun saveLocalMessages(messages: List<Message>)
}
