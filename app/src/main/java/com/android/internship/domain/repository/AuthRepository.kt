package com.android.internship.domain.repository

interface AuthRepository {
    suspend fun signIn(email: String, password: String): Result<String>
    fun isSignedIn(): Boolean
    fun getCurrentUserId(): String?
}
