package com.android.internship.data.repository

import com.android.internship.data.datasource.local.AuthLocalDataSource
import com.android.internship.data.datasource.remote.AuthRemoteDataSource
import com.android.internship.data.model.SignInResponse
import com.android.internship.domain.repository.AuthRepository

class AuthRepositoryImpl(
    private val remoteDataSource: AuthRemoteDataSource,
    private val localDataSource: AuthLocalDataSource,
) : AuthRepository {

    override suspend fun signIn(email: String, password: String): SignInResponse {
        return try {
            val user = remoteDataSource.signInWithEmailAndPassword(email, password)
            if (user != null) {
                SignInResponse(
                    success = true,
                    message = "Sign in successful",
                    user = user,
                )
            } else {
                SignInResponse(
                    success = false,
                    message = "Invalid credentials",
                    user = null,
                )
            }
        } catch (e: Exception) {
            SignInResponse(
                success = false,
                message = e.message ?: "Sign-in failed",
                user = null,
            )
        }
    }

    override suspend fun isSignedIn(): Boolean? {
        return localDataSource.isUserSignedIn()
    }
}
