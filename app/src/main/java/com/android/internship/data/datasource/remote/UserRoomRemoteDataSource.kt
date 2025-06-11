package com.android.internship.data.datasource.remote

import android.util.Log
import com.android.internship.data.model.UserRoom
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class UserRoomRemoteDataSource {
    private val firestore = FirebaseFirestore.getInstance()
    private val userRoomsCollection = firestore.collection("userRooms")

    fun addUserRoomRemote(userRoom: UserRoom) {
        firestore.collection("userRooms")
            .document("${userRoom.rid}_${userRoom.uid}")
            .set(userRoom)
    }

    suspend fun getUserRoomsForUser(uid: String): List<UserRoom> {
        try {
            val snapshot = firestore.collection("userRooms")
                .whereEqualTo("uid", uid)
                .get()
                .await()
            val userRooms = snapshot.documents.mapNotNull { it.toObject<UserRoom>() }
            Log.d("UserRoomRemoteDataSource", "Found ${userRooms.size} rooms for user $uid")
            return userRooms
        } catch (e: Exception) {
            Log.e("UserRoomRemoteDataSource", "Error getting user rooms: ${e.message}")
            return emptyList()
        }
    }

    suspend fun getUserRoomsForRoom(rid: String): List<UserRoom> {
        try {
            val snapshot = firestore.collection("userRooms")
                .whereEqualTo("rid", rid)
                .get()
                .await()
            val userRooms = snapshot.documents.mapNotNull { it.toObject<UserRoom>() }
            Log.d("UserRoomRemoteDataSource", "Found ${userRooms.size} users in room $rid")
            return userRooms
        } catch (e: Exception) {
            Log.e("UserRoomRemoteDataSource", "Error getting room users: ${e.message}")
            return emptyList()
        }
    }

    fun updateMute(rid: String, uid: String, mute: Boolean, turnOnTime: String?) {
        firestore.collection("userRooms")
            .document("${rid}_$uid")
            .update("mute", mute, "turnOnTime", turnOnTime)
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

    fun observeUserRoomsForRoom(rid: String): Flow<List<UserRoom>> {
        return callbackFlow {
            val query = userRoomsCollection.whereEqualTo("rid", rid)

            val listenerRegistration = query.addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val userRooms = snapshot.toObjects(UserRoom::class.java)
                    trySend(userRooms)
                }
            }

            awaitClose { listenerRegistration.remove() }
        }
    }
}
