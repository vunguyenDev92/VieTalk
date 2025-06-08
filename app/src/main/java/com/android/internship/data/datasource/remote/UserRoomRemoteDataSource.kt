package com.android.internship.data.datasource.remote

import com.android.internship.data.model.UserRoom
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.tasks.await

class UserRoomRemoteDataSource {
    private val firestore = FirebaseFirestore.getInstance()

    fun addUserRoomRemote(userRoom: UserRoom) {
        firestore.collection("userRooms")
            .document("${userRoom.rid}_${userRoom.uid}")
            .set(userRoom)
    }

    suspend fun getUserRoomRemote(rid: String, uid: String): UserRoom? {
        val snapshot = firestore.collection("userRooms")
            .document("${rid}_$uid")
            .get()
            .await()
        return snapshot.toObject<UserRoom>()
    }

    suspend fun getUserRoomsForRoom(rid: String): List<UserRoom> {
        val snapshot = firestore.collection("userRooms")
            .whereEqualTo("rid", rid)
            .get()
            .await()
        return snapshot.documents.mapNotNull { it.toObject<UserRoom>() }
    }

    fun updateTypingTime(rid: String, uid: String, time: String) {
        firestore.collection("userRooms")
            .document("${rid}_$uid")
            .update("typingTime", time)
    }

    fun updateLastSeenMessages(rid: String, uid: String, lastSeenMessages: String?) {
        firestore.collection("userRooms")
            .document("${rid}_$uid")
            .update("lastSeenMessages", lastSeenMessages)
    }
}
