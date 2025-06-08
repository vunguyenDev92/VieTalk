package com.android.internship.data.datasource.remote

import com.android.internship.data.model.Room
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.tasks.await

class RoomRemoteDataSource {
    private val firestore = FirebaseFirestore.getInstance()

    suspend fun getRoomById(rid: String): Room? {
        val snapshot = firestore.collection("rooms")
            .document(rid)
            .get()
            .await()
        return snapshot.toObject<Room>()
    }

    suspend fun getAllRoom(): List<Room>? {
        val snapshot = firestore.collection("rooms")
            .get()
            .await()
        return snapshot.documents.mapNotNull { it.toObject<Room>() }
    }
}
