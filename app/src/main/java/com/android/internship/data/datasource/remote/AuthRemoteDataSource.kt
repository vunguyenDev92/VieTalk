package com.android.internship.data.datasource.remote

import com.android.internship.data.model.User
import com.android.internship.data.model.UserRoom
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class AuthRemoteDataSource {
    private val auth = FirebaseAuth.getInstance()
    private val fireStore = FirebaseFirestore.getInstance()

    suspend fun signInWithEmailAndPassword(email: String, password: String): Result<String> {
        try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            return Result.success(result.user?.uid ?: "")
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    private suspend fun getUserFromFireStore(uid: String): User? {
        return try {
            val userDoc = fireStore.collection("users")
                .document(uid)
                .get()
                .await()

            if (userDoc.exists()) {
                User(
                    uid = userDoc.id,
                    username = userDoc.getString("username") ?: "",
                    email = userDoc.getString("email") ?: "",
                    active = userDoc.getBoolean("active") == true,
                    avatar = userDoc.getString("avatar"),
                    block = userDoc.get("block") as? List<String>,
                    userRooms = parseUserRooms(userDoc.get("userRooms")),
                )
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

    private fun parseUserRooms(userRoomsData: Any?): List<UserRoom>? {
        return when (userRoomsData) {
            is List<*> -> {
                userRoomsData.mapNotNull { item ->
                    if (item is Map<*, *>) {
                        UserRoom(
                            rid = item["rid"] as? String ?: "",
                            mute = item["mute"] as? Boolean == true,
                            turnOnTime = item["turnOnTime"] as? String ?: "",
                            uid = item["uid"] as? String ?: "",
                            lastSeenMessages = item["lastSeenMessages"] as? String ?: "",
                        )
                    } else {
                        null
                    }
                }
            }
            else -> null
        }
    }
}
