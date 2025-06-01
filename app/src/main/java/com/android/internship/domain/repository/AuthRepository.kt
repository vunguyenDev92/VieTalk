package com.android.internship.domain.repository

import com.android.internship.data.model.SignInResponse

interface AuthRepository {
    suspend fun signIn(username: String, password: String): SignInResponse
    fun getCurrentUser(): String?
}
