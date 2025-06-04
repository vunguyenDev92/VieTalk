package com.android.internship.data.datasource.remote

import com.android.internship.data.datasource.local.entity.MessageEntity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.tasks.await

class MessageRemoteDataSource {
    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    fun isUserSignedIn(): Boolean {
        return auth.currentUser != null
    }

    suspend fun saveMessage(message: MessageEntity) {
        if (!isUserSignedIn()) throw SecurityException("User is not signed in")

        firestore.collection("messages")
            .document(message.mid)
            .set(message)
            .await()
    }

    suspend fun getLatestMessagesForRoom(rid: String): List<MessageEntity> {
        if (!isUserSignedIn()) return emptyList()

        val snapshot = firestore.collection("messages")
            .whereEqualTo("rid", rid)
            .orderBy("time", Query.Direction.DESCENDING)
            .limit(20)
            .get()
            .await()

        val messages = snapshot.documents.mapNotNull { it.toObject<MessageEntity>() }
        return messages.sortedBy { it.time }
    }

    suspend fun getOlderMessagesForRoom(rid: String, lastMessageTime: String): List<MessageEntity> {
        if (!isUserSignedIn()) return emptyList()

        val snapshot = firestore.collection("messages")
            .whereEqualTo("rid", rid)
            .whereLessThan("time", lastMessageTime)
            .orderBy("time", Query.Direction.DESCENDING)
            .limit(20)
            .get()
            .await()

        val messages = snapshot.documents.mapNotNull { it.toObject<MessageEntity>() }
        return messages.sortedBy { it.time }
    }
}
