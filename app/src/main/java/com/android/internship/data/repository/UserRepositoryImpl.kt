package com.android.internship.data.repository

import com.android.internship.data.datasource.local.UserLocalDataSource
import com.android.internship.data.datasource.remote.UserRemoteDataSource
import com.android.internship.data.model.User
import com.android.internship.domain.repository.UserRepository

class UserRepositoryImpl(
    private val userLocalDataSource: UserLocalDataSource,
    private val userRemoteDataSource: UserRemoteDataSource,
) : UserRepository {

    override fun addUserRemote(
        uid: String,
        username: String,
        lastActiveTime: String,
        avatar: String?,
    ) {
        val user = User(
            uid = uid,
            username = username,
            lastActiveTime = lastActiveTime,
            avatar = avatar,
        )

        userRemoteDataSource.addUserToFireStore(user)
    }

    override suspend fun getUserRemote(uid: String): User? {
        return userRemoteDataSource.getUserFromFireStore(uid)
    }

    override suspend fun getAllUserRemote(): List<User>? {
        return userRemoteDataSource.getAllUserFromFireStore()
    }

    override fun updateActiveTime(uid: String, lastActiveTime: String) {
        userRemoteDataSource.updateActiveUser(uid = uid, lastActiveTime = lastActiveTime)
    }

    override fun updateAvatar(uid: String, avatar: String) {
        userRemoteDataSource.updateAvatar(uid = uid, avatar = avatar)
    }

    override suspend fun getUserLocal(uid: String): User? {
        return userLocalDataSource.getUser(uid)
    }

    override suspend fun getAllUserLocal(): List<User>? {
        return userLocalDataSource.getAllUsers()
    }

    override suspend fun saveLocalUser(user: User) {
        userLocalDataSource.insertUser(user)
    }

    override suspend fun saveLocalUsers(users: List<User>) {
        userLocalDataSource.insertUsers(users)
    }

    override suspend fun getUsersInfo(uids: List<String>): List<User> {
        return userRemoteDataSource.getUsersInRoomFromFireStore(uids)
    }
}
