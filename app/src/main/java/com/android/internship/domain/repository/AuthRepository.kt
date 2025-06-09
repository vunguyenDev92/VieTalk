package com.android.internship.domain.repository

import com.android.internship.data.model.SignInResponse
import com.android.internship.data.model.User

interface AuthRepository {
    suspend fun signIn(email: String, password: String): SignInResponse
    fun isSignedIn(): Boolean
    suspend fun getLastActiveTimeUser(uid: String): String?
    fun updateActiveUser(uid: String, lastActiveTime: String)
    fun setMuteGroup(rid: String, uid: String, time: String?)
    suspend fun getUserInfo(uid: String): User?
    fun getCurrentUserId(): String?
	suspend fun getUsersInfo(uids: List<String>): List<User>
}
