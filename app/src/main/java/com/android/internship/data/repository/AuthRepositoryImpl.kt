package com.android.internship.data.repository

import com.android.internship.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await

class AuthRepositoryImpl : AuthRepository {
    private val auth = FirebaseAuth.getInstance()

    override suspend fun signIn(
        email: String,
        password: String,
    ): Result<String> {
        try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            return Result.success(result.user?.uid ?: "")
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    override fun isSignedIn(): Boolean {
        return auth.currentUser != null
    }

    override fun getCurrentUserId(): String? {
        return auth.currentUser?.uid
    }
}
