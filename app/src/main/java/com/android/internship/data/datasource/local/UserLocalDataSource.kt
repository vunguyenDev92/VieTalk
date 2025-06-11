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
            UserEntity(
                uid = user.uid,
                username = user.username,
                lastActiveTime = user.lastActiveTime,
                avatar = user.avatar,
            ),
        )
    }

    suspend fun insertUsers(users: List<User>) {
        val userEntities = users.map {
            UserEntity(
                uid = it.uid,
                username = it.username,
                lastActiveTime = it.lastActiveTime,
                avatar = it.avatar,
            )
        }
        userDao.insertUsers(userEntities)
    }

    suspend fun getUser(uid: String): User? {
        return userDao.getUser(uid)?.let {
            User(
                uid = it.uid,
                username = it.username,
                lastActiveTime = it.lastActiveTime,
                avatar = it.avatar,
            )
        }
    }

    suspend fun getAllUsers(): List<User>? {
        return userDao.getAllUsers()?.map {
            User(
                uid = it.uid,
                username = it.username,
                lastActiveTime = it.lastActiveTime,
                avatar = it.avatar,
            )
        }
    }
}
