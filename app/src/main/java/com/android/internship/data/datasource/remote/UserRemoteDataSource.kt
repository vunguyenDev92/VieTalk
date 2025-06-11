package com.android.internship.data.datasource.remote

import com.android.internship.data.model.User
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class UserRemoteDataSource {
    private val fireStore = FirebaseFirestore.getInstance()

    fun addUserToFireStore(user: User) {
        fireStore.collection("users").document(user.uid).set(user)
    }

    suspend fun getUserFromFireStore(uid: String): User? {
        val snapshot = fireStore.collection("users").document(uid).get().await()
        return snapshot.toObject(User::class.java)
    }

    suspend fun getAllUserFromFireStore(): List<User>? {
        val snapshot = fireStore.collection("users").get().await()
        return snapshot.documents.mapNotNull { it.toObject(User::class.java) }
    }

    fun updateActiveUser(uid: String, lastActiveTime: String) {
        fireStore.collection("users").document(uid).update("lastActiveTime", lastActiveTime)
    }

    fun updateAvatar(uid: String, avatar: String) {
        fireStore.collection("users").document(uid).update("avatar", avatar)
    }
}
