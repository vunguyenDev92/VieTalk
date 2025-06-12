package com.android.internship.data.datasource.remote

import com.android.internship.data.model.Message
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await

class MessageRemoteDataSource {
    private val firestore = FirebaseFirestore.getInstance()
    private val messagesCollection = firestore.collection("messages")

    fun addRemoteMessage(message: Message) {
        firestore.collection("messages")
            .document(message.mid)
            .set(message)
    }

    fun observeMessages(rid: String): Flow<List<Message>> {
        return callbackFlow {
            val query = messagesCollection
                .whereEqualTo("rid", rid)
                .orderBy("time", Query.Direction.ASCENDING)

            val listenerRegistration = query.addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val messages = snapshot.documents.mapNotNull { it.toObject<Message>() }
                    trySend(messages)
                }
            }

            awaitClose {
                listenerRegistration.remove()
            }
        }
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

    fun observeNewMessages(roomId: String, afterTimestamp: Long): Flow<List<Message>> {
        return callbackFlow {
            val listener = firestore
                .collection("messages")
                .whereEqualTo("rid", roomId)
                .whereGreaterThan("time", afterTimestamp)
                .orderBy("time", Query.Direction.ASCENDING)
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        close(error)
                        return@addSnapshotListener
                    }

                    val messages = snapshot?.documents?.mapNotNull { doc ->
                        doc.toObject(Message::class.java)
                    } ?: emptyList()

                    trySend(messages)
                }

            awaitClose { listener.remove() }
        }.flowOn(Dispatchers.IO)
    }
}
