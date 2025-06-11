package com.android.internship.di

import android.content.Context
import com.android.internship.data.datasource.local.AuthLocalDataSource
import com.android.internship.data.datasource.local.MessageLocalDataSource
import com.android.internship.data.datasource.local.RoomLocalDataSource
import com.android.internship.data.datasource.local.UserRoomLocalDataSource
import com.android.internship.data.datasource.remote.AuthRemoteDataSource
import com.android.internship.data.datasource.remote.MessageRemoteDataSource
import com.android.internship.data.datasource.remote.RoomRemoteDataSource
import com.android.internship.data.datasource.remote.UserRoomRemoteDataSource
import com.android.internship.data.repository.AuthRepositoryImpl
import com.android.internship.data.repository.RoomRepositoryImpl
import com.android.internship.domain.repository.AuthRepository
import com.android.internship.domain.repository.RoomRepository
import com.android.internship.utils.ConnectivityObserver
import com.android.internship.utils.IConnectivityObserver

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

    private val roomRemoteDataSource: RoomRemoteDataSource by lazy {
        RoomRemoteDataSource()
    }

    private val roomLocalDataSource: RoomLocalDataSource by lazy {
        RoomLocalDataSource(appContext)
    }

    private val userRoomRemoteDataSource: UserRoomRemoteDataSource by lazy {
        UserRoomRemoteDataSource()
    }

    private val userRoomLocalDataSource: UserRoomLocalDataSource by lazy {
        UserRoomLocalDataSource(appContext)
    }

    private val messageLocalDataSource: MessageLocalDataSource by lazy {
        MessageLocalDataSource(appContext)
    }

    private val messageRemoteDataSource: MessageRemoteDataSource by lazy {
        MessageRemoteDataSource()
    }

    val roomRepository: RoomRepository by lazy {
        RoomRepositoryImpl(roomLocalDataSource, roomRemoteDataSource, messageLocalDataSource, messageRemoteDataSource, userRoomRemoteDataSource, userRoomLocalDataSource)
    }

    val connectivityObserver: IConnectivityObserver by lazy {
        ConnectivityObserver(context.applicationContext)
    }
}
