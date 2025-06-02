package com.android.internship.domain.repository

import com.android.internship.data.model.SignInResponse

interface AuthRepository {
    suspend fun signIn(email: String, password: String): SignInResponse
    fun isSignedIn(): Boolean?
}
