package com.android.internship.data.datasource.remote

import com.android.internship.data.model.User
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

    suspend fun getUserFromFireStore(uid: String): User? {
        return try {
            val userDoc = fireStore.collection("users")
                .document(uid)
                .get()
                .await()

            if (userDoc.exists()) {
                userDoc.toObject(User::class.java)
            } else {
                null
            }
        } catch (_: Exception) {
            null
        }
    }

    suspend fun getActiveUser(uid: String): Boolean {
        return try {
            fireStore.collection("users").document(uid).get().await().getBoolean("active") == true
        } catch (_: Exception) {
            false
        }
    }

    fun updateActiveUser(uid: String, lastActiveTime: String) {
        try {
            fireStore.collection("users").document(uid).update("lastActiveTime", lastActiveTime)
        } catch (_: Exception) {}
    }

    fun updateMuteUser(rid: String, uid: String, time: String?) {
        try {
            fireStore.collection("rooms").document(rid).collection("users").document(uid)
                .update("mute", time != null, "turnOnTime", time)
        } catch (_: Exception) {}
    }
}
