package com.android.internship.di

import android.content.Context
import com.android.internship.data.datasource.local.BlockLocalDataSource
import com.android.internship.data.datasource.local.MessageLocalDataSource
import com.android.internship.data.datasource.local.RoomLocalDataSource
import com.android.internship.data.datasource.local.UserLocalDataSource
import com.android.internship.data.datasource.local.UserRoomLocalDataSource
import com.android.internship.data.datasource.remote.BlockRemoteDataSource
import com.android.internship.data.datasource.remote.MessageRemoteDataSource
import com.android.internship.data.datasource.remote.RoomRemoteDataSource
import com.android.internship.data.datasource.remote.UserRemoteDataSource
import com.android.internship.data.datasource.remote.UserRoomRemoteDataSource
import com.android.internship.data.repository.AuthRepositoryImpl
import com.android.internship.data.repository.BlockRepositoryImpl
import com.android.internship.data.repository.MessageRepositoryImpl
import com.android.internship.data.repository.RoomRepositoryImpl
import com.android.internship.data.repository.UserRepositoryImpl
import com.android.internship.data.repository.UserRoomRepositoryImpl
import com.android.internship.domain.repository.AuthRepository
import com.android.internship.domain.repository.BlockRepository
import com.android.internship.domain.repository.MessageRepository
import com.android.internship.domain.repository.RoomRepository
import com.android.internship.domain.repository.UserRepository
import com.android.internship.domain.repository.UserRoomRepository

class AppContainer(context: Context) {
    private val appContext = context.applicationContext

    // Auth
    val authRepository: AuthRepository by lazy {
        AuthRepositoryImpl()
    }

    // User
    private val userLocalDataSource: UserLocalDataSource by lazy {
        UserLocalDataSource(appContext)
    }

    private val userRemoteDataSource: UserRemoteDataSource by lazy {
        UserRemoteDataSource()
    }

    val userRepository: UserRepository by lazy {
        UserRepositoryImpl(userLocalDataSource, userRemoteDataSource)
    }

    // Room
    private val roomLocalDataSource: RoomLocalDataSource by lazy {
        RoomLocalDataSource(appContext)
    }

    private val roomRemoteDataSource: RoomRemoteDataSource by lazy {
        RoomRemoteDataSource()
    }

    val roomRepository: RoomRepository by lazy {
        RoomRepositoryImpl(roomLocalDataSource, roomRemoteDataSource)
    }

    // Message
    private val messageLocalDataSource: MessageLocalDataSource by lazy {
        MessageLocalDataSource(appContext)
    }

    private val messageRemoteDataSource: MessageRemoteDataSource by lazy {
        MessageRemoteDataSource()
    }

    val messageRepository: MessageRepository by lazy {
        MessageRepositoryImpl(messageLocalDataSource, messageRemoteDataSource)
    }

    // UserRoom
    private val userRoomLocalDataSource: UserRoomLocalDataSource by lazy {
        UserRoomLocalDataSource(appContext)
    }

    private val userRoomRemoteDataSource: UserRoomRemoteDataSource by lazy {
        UserRoomRemoteDataSource()
    }

    val userRoomRepository: UserRoomRepository by lazy {
        UserRoomRepositoryImpl(userRoomLocalDataSource, userRoomRemoteDataSource)
    }

    // Block
    private val blockLocalDataSource: BlockLocalDataSource by lazy {
        BlockLocalDataSource(appContext)
    }

    private val blockRemoteDataSource: BlockRemoteDataSource by lazy {
        BlockRemoteDataSource()
    }

    val blockRepository: BlockRepository by lazy {
        BlockRepositoryImpl(blockLocalDataSource, blockRemoteDataSource)
    }
}
