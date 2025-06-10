package com.android.internship.data.repository

import com.android.internship.data.datasource.local.BlockLocalDataSource
import com.android.internship.data.datasource.remote.BlockRemoteDataSource
import com.android.internship.domain.repository.BlockRepository

class BlockRepositoryImpl(
    private val blockLocalDataSource: BlockLocalDataSource,
    private val blockRemoteDataSource: BlockRemoteDataSource,
) : BlockRepository {
    override suspend fun blockUserRemote(uid: String, blockedUid: String) {
        blockRemoteDataSource.blockUserRemote(uid, blockedUid)
    }

    override suspend fun unblockUserRemote(uid: String, blockedUid: String) {
        blockRemoteDataSource.unblockUserRemote(uid, blockedUid)
    }

    override suspend fun isBlockedRemote(
        uid: String,
        blockedUid: String,
    ): Boolean {
        return blockRemoteDataSource.isBlockedRemote(uid, blockedUid)
    }

    override suspend fun isBlockedLocal(
        uid: String,
        blockedUid: String,
    ): Boolean {
        return blockLocalDataSource.isBlockedLocal(uid, blockedUid)
    }

    override suspend fun saveLocalBlock(uid: String, blockedUid: String) {
        blockLocalDataSource.blockUserLocal(uid, blockedUid)
    }

    override suspend fun deleteLocalBlock(uid: String, blockedUid: String) {
        blockLocalDataSource.deleteBlockUserLocal(uid, blockedUid)
    }
}
