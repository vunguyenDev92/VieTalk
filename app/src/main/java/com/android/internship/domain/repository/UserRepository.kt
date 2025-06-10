package com.android.internship.domain.repository

import com.android.internship.data.model.User

interface UserRepository {
    // User Remote
    fun addUserRemote(uid: String, username: String, lastActiveTime: String = "", avatar: String? = null)
    suspend fun getUserRemote(uid: String): User?
    suspend fun getAllUserRemote(): List<User>?
    fun updateActiveTime(uid: String, lastActiveTime: String)
    fun updateAvatar(uid: String, avatar: String)

    // User Local
    suspend fun getUserLocal(uid: String): User?
    suspend fun getAllUserLocal(): List<User>?
    suspend fun saveLocalUser(user: User)
    suspend fun saveLocalUsers(users: List<User>)
}
