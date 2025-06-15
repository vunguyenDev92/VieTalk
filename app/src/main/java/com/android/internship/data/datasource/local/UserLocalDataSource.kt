package com.android.internship.data.datasource.local

import android.content.Context
import com.android.internship.data.datasource.local.database.AppDatabase
import com.android.internship.data.datasource.local.entity.UserEntity
import com.android.internship.data.model.User

class UserLocalDataSource(context: Context) {
    private val database = AppDatabase.getDatabase(context)
    private val userDao = database.userDao()

    suspend fun insertUser(user: User) {
        userDao.insertUser(
            UserEntity.fromUser(user),
        )
    }

    suspend fun insertUsers(users: List<User>) {
        userDao.insertUsers(
            users.map { user ->
                UserEntity.fromUser(user)
            },
        )
    }

    suspend fun getUser(uid: String): User? {
        return userDao.getUser(uid)?.toUser()
    }

    suspend fun getAllUsers(): List<User>? {
        return userDao.getAllUsers().map {
            it.toUser()
        }
    }
}
