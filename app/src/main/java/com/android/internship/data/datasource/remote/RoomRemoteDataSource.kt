package com.android.internship.data.datasource.remote

import com.android.internship.data.model.Room
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class RoomRemoteDataSource {
    private val firestore = FirebaseFirestore.getInstance()
    private val roomsCollection = firestore.collection("rooms")

    fun addRoom(room: Room) {
        roomsCollection.document(room.rid).set(room)
    }

    suspend fun getRooms(rids: List<String>): List<Room>? {
        val snapshot = roomsCollection
            .whereIn("rid", rids)
            .get()
            .await()
        return snapshot.documents.mapNotNull { it.toObject<Room>() }
    }

    fun observeRooms(): Flow<List<Room>> {
        return callbackFlow {
            val query = roomsCollection.orderBy("updatedAt", Query.Direction.DESCENDING)

            val listenerRegistration = query.addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val rooms = snapshot.documents.mapNotNull { it.toObject<Room>() }
                    trySend(rooms)
                }
            }

            awaitClose { listenerRegistration.remove() }
        }
    }
}
