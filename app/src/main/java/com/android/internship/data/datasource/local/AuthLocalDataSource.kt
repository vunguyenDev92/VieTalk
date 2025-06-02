package com.android.internship.data.datasource.local

import android.content.Context
import com.android.internship.data.datasource.local.database.AppDatabase
import com.android.internship.data.datasource.local.entity.UserEntity
import com.android.internship.data.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AuthLocalDataSource(context: Context) {
    private val database = AppDatabase.getDatabase(context)
    private val userDao = database.userDao()

    suspend fun saveUserSession(user: User) = withContext(Dispatchers.IO) {
        userDao.clearCurrentUserFlag()

        val userEntity = UserEntity(
            uid = user.uid,
            username = user.username,
            email = user.email,
            active = user.active,
            avatar = user.avatar,
            block = user.block,
            userRooms = user.userRooms,
            isCurrentUser = true,
        )
        userDao.saveUser(userEntity)
    }

    suspend fun getCurrentUser(): User? = withContext(Dispatchers.IO) {
        val userEntity = userDao.getCurrentUser()
        userEntity?.let {
            User(
                uid = it.uid,
                username = it.username,
                email = it.email,
                active = it.active,
                avatar = it.avatar,
                block = it.block,
                userRooms = it.userRooms,
            )
        }
    }

    suspend fun getCurrentUserId(): String? = withContext(Dispatchers.IO) {
        userDao.getCurrentUserId()
    }

    suspend fun isLoggedIn(): Boolean = withContext(Dispatchers.IO) {
        userDao.hasCurrentUser()
    }

    suspend fun clearUserSession() = withContext(Dispatchers.IO) {
        userDao.deleteCurrentUser()
    }

    suspend fun getCurrentUsername(): String? = withContext(Dispatchers.IO) {
        userDao.getCurrentUser()?.username
    }

    suspend fun getCurrentUserEmail(): String? = withContext(Dispatchers.IO) {
        userDao.getCurrentUser()?.email
    }
}
