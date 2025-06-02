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
        val result = remoteDataSource.signInWithEmailAndPassword(email, password)
        if (result.isFailure) {
            return SignInResponse(
                success = false,
                message = result.exceptionOrNull()?.message ?: "Sign-in failed",
            )
        }
        return SignInResponse(
            success = true,
            message = "Sign-in successful",
        )
    }

    override fun isSignedIn(): Boolean? {
        return localDataSource.isUserSignedIn()
    }
}
