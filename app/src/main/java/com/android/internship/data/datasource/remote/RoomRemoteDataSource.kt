package com.android.internship.data.datasource.remote

import com.android.internship.data.model.Room
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.tasks.await

class RoomRemoteDataSource {
    private val firestore = FirebaseFirestore.getInstance()

    fun addRoom(room: Room) {
        firestore.collection("rooms")
            .document(room.rid)
            .set(room)
    }

    suspend fun getRooms(rids: List<String>): List<Room>? {
        val snapshot = firestore.collection("rooms")
            .whereIn("rid", rids)
            .get()
            .await()
        return snapshot.documents.mapNotNull { it.toObject<Room>() }
    }
}
