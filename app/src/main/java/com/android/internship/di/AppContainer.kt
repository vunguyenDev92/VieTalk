package com.android.internship.di

import android.content.Context
import com.android.internship.data.datasource.local.AuthLocalDataSource
import com.android.internship.data.datasource.remote.AuthRemoteDataSource
import com.android.internship.data.repository.AuthRepositoryImpl
import com.android.internship.domain.repository.AuthRepository

class AppContainer(context: Context) {
    private val appContext = context.applicationContext

    private val authRemoteDataSource: AuthRemoteDataSource by lazy {
        AuthRemoteDataSource()
    }

    private val authLocalDataSource: AuthLocalDataSource by lazy {
        AuthLocalDataSource(appContext)
    }

    val authRepository: AuthRepository by lazy {
        AuthRepositoryImpl(authRemoteDataSource, authLocalDataSource)
    }
}
