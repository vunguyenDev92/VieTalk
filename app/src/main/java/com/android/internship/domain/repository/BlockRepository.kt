package com.android.internship.domain.repository

interface BlockRepository {
    // Block Remote
    suspend fun blockUserRemote(uid: String, blockedUid: String)
    suspend fun unblockUserRemote(uid: String, blockedUid: String)
    suspend fun isBlockedRemote(uid: String, blockedUid: String): Boolean

    // Block Local
    suspend fun isBlockedLocal(uid: String, blockedUid: String): Boolean
    suspend fun saveLocalBlock(uid: String, blockedUid: String)
    suspend fun deleteLocalBlock(uid: String, blockedUid: String)
}
