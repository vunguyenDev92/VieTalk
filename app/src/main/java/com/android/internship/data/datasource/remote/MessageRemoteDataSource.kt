package com.android.internship.data.datasource.remote

import com.android.internship.data.model.Message
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.tasks.await

class MessageRemoteDataSource {
    private val firestore = FirebaseFirestore.getInstance()

    fun addRemoteMessage(rid: String, message: Message) {
        firestore.collection("messages")
            .document(message.mid)
            .set(message)
    }

    suspend fun getRemoteMessages(
        rid: String,
        startMessageId: String?,
        limit: Int,
    ): List<Message>? {
        try {
            var query = firestore.collection("messages")
                .whereEqualTo("rid", rid)
                .orderBy("time", Query.Direction.DESCENDING)
                .limit(limit.toLong())

            if (startMessageId != null) {
                query = query.startAfter(startMessageId)
            }

            val snapshot = query.get().await()
            val messages = snapshot.documents.mapNotNull { it.toObject<Message>() }
            return messages
        } catch (e: Exception) {
            return null
        }
    }
}
