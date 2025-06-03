package com.android.internship.data.datasource.remote

import com.android.internship.data.datasource.local.entity.RoomEntity
import com.android.internship.data.datasource.local.entity.UserRoomEntity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.tasks.await

class RoomRemoteDataSource {
    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    fun isUserSignedIn(): Boolean {
        return auth.currentUser != null
    }

    suspend fun saveRoom(room: RoomEntity) {
        if (!isUserSignedIn()) throw SecurityException("User is not signed in")
        firestore.collection("rooms")
            .whereEqualTo("rid", room.rid)
            .get()
            .await()
            .size()
    }

    suspend fun getRoomById(rid: String): RoomEntity? {
        if (!isUserSignedIn()) return null
        val snapshot = firestore.collection("rooms")
            .document(rid)
            .get()
            .await()
        return snapshot.toObject<RoomEntity>()
    }

    suspend fun getAllRoom(): List<RoomEntity>? {
        if (!isUserSignedIn()) return emptyList()
        val snapshot = firestore.collection("rooms")
            .get()
            .await()
        return snapshot.documents.mapNotNull { it.toObject<RoomEntity>() }
    }

    suspend fun insertUserRoom(userRoom: UserRoomEntity) {
        if (!isUserSignedIn()) throw SecurityException("User is not signed in")
        firestore.collection("user_rooms")
            .document("${userRoom.rid}_${userRoom.uid}")
            .set(userRoom)
            .await()
    }

    suspend fun getUserRoomsForRoom(rid: String): List<UserRoomEntity> {
        if (!isUserSignedIn()) return emptyList()
        val snapshot = firestore.collection("user_rooms")
            .whereEqualTo("rid", rid)
            .get()
            .await()
        return snapshot.documents.mapNotNull { it.toObject<UserRoomEntity>() }
    }

    suspend fun getUserRoom(rid: String, uid: String): UserRoomEntity? {
        if (!isUserSignedIn()) return null
        val snapshot = firestore.collection("user_rooms")
            .document("${rid}_$uid")
            .get()
            .await()
        return snapshot.toObject<UserRoomEntity>()
    }

    suspend fun getTypingUsersInRoom(rid: String): List<UserRoomEntity> {
        if (!isUserSignedIn()) return emptyList()
        val snapshot = firestore.collection("user_rooms")
            .whereEqualTo("rid", rid)
            .whereEqualTo("isTyping", true)
            .get()
            .await()
        return snapshot.documents.mapNotNull { it.toObject<UserRoomEntity>() }
    }

    suspend fun updateIsTyping(rid: String, uid: String, isTyping: Boolean) {
        if (!isUserSignedIn()) throw SecurityException("User is not signed in")
        firestore.collection("user_rooms")
            .document("${rid}_$uid")
            .update("isTyping", isTyping)
            .await()
    }

    suspend fun updateLastSeenMessages(rid: String, uid: String, lastSeenMessages: String?) {
        if (!isUserSignedIn()) throw SecurityException("User is not signed in")
        firestore.collection("user_rooms")
            .document("${rid}_$uid")
            .update("lastSeenMessages", lastSeenMessages)
            .await()
    }
}
