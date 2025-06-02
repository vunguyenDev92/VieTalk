package com.android.internship.data.datasource.remote

import com.android.internship.data.model.User
import com.android.internship.data.model.UserRoom
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class AuthRemoteDataSource(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
) {

    suspend fun signInWithEmailAndPassword(email: String, password: String): User? {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            val firebaseUser = result.user

            if (firebaseUser != null) {
                getUserFromFirestore(firebaseUser.uid)
            } else {
                null
            }
        } catch (e: Exception) {
            throw e
        }
    }

    private suspend fun getUserFromFirestore(uid: String): User? {
        return try {
            val userDoc = firestore.collection("users")
                .document(uid)
                .get()
                .await()

            if (userDoc.exists()) {
                User(
                    uid = userDoc.id,
                    username = userDoc.getString("username") ?: "",
                    email = userDoc.getString("email") ?: "",
                    active = userDoc.getBoolean("active") ?: false,
                    avatar = userDoc.getString("avatar"),
                    block = userDoc.get("block") as? List<String>,
                    userRooms = parseUserRooms(userDoc.get("userRooms")),
                )
            } else {
                null
            }
        } catch (e: Exception) {
            throw e
        }
    }

    private fun parseUserRooms(userRoomsData: Any?): List<UserRoom>? {
        return try {
            when (userRoomsData) {
                is List<*> -> {
                    userRoomsData.mapNotNull { item ->
                        if (item is Map<*, *>) {
                            UserRoom(
                                rid = item["rid"] as? String ?: "",
                                mute = item["mute"] as? Boolean ?: false,
                                turnOnTime = item["turnOnTime"] as? String ?: "",
                            )
                        } else {
                            null
                        }
                    }
                }
                else -> null
            }
        } catch (e: Exception) {
            null
        }
    }

    fun signOut() {
        auth.signOut()
    }

    fun isUserSignedIn(): Boolean {
        return auth.currentUser != null
    }

    fun getCurrentUserId(): String? {
        return auth.currentUser?.uid
    }
}

private fun User.toMap(): Map<String, Any?> {
    return mapOf(
        "username" to username,
        "email" to email,
        "active" to active,
        "avatar" to avatar,
        "block" to block,
        "userRooms" to userRooms?.map { userRoom ->
            mapOf(
                "rid" to userRoom.rid,
                "mute" to userRoom.mute,
                "turnOnTime" to userRoom.turnOnTime,
            )
        },
    )
}
