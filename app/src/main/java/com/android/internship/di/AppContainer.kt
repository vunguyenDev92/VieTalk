package com.android.internship.di

import android.content.Context
import com.android.internship.data.datasource.local.AuthLocalDataSource
import com.android.internship.data.datasource.remote.AuthRemoteDataSource
import com.android.internship.data.repository.AuthRepositoryImpl
import com.android.internship.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AppContainer(context: Context) {
    private val appContext = context.applicationContext

    private val authRemoteDataSource: AuthRemoteDataSource by lazy {
        AuthRemoteDataSource(
            auth = FirebaseAuth.getInstance(),
            firestore = FirebaseFirestore.getInstance(),
        )
    }

    private val authLocalDataSource: AuthLocalDataSource by lazy {
        AuthLocalDataSource(appContext)
    }

    val authRepository: AuthRepository by lazy {
        AuthRepositoryImpl(authRemoteDataSource, authLocalDataSource)
    }
}
