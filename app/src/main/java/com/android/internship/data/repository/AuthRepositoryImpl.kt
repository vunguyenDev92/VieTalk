package com.android.internship.data.repository

import com.android.internship.data.datasource.local.AuthLocalDataSource
import com.android.internship.data.datasource.remote.AuthRemoteDataSource
import com.android.internship.data.model.SignInResponse
import com.android.internship.domain.repository.AuthRepository

class AuthRepositoryImpl(
    private val authRemoteDataSource: AuthRemoteDataSource,
    private val authLocalDataSource: AuthLocalDataSource,
) : AuthRepository {

    override suspend fun signIn(
        username: String,
        password: String,
    ): SignInResponse {
        TODO("Not yet implemented")
    }

    override fun getCurrentUser(): String? {
        TODO("Not yet implemented")
    }
}
