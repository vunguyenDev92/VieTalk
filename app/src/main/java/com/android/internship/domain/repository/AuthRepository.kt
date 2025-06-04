package com.android.internship.domain.repository

import com.android.internship.data.model.SignInResponse
import com.android.internship.data.model.User

interface AuthRepository {
    suspend fun signIn(email: String, password: String): SignInResponse
    fun isSignedIn(): Boolean?
    suspend fun getActiveUser(uid: String): Boolean
    suspend fun setActiveUser(uid: String, isActive: Boolean)
    suspend fun setMuteGroup(rid: String, uid: String, time: String?)
    suspend fun getUserInfo(uid: String): User
    fun getCurrentUserId(): String
}
