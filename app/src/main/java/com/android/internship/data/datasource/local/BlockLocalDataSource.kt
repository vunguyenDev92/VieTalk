package com.android.internship.data.datasource.local

import android.content.Context
import com.android.internship.data.datasource.local.database.AppDatabase
import com.android.internship.data.datasource.local.entity.BlockEntity

class BlockLocalDataSource(context: Context) {
    private val database = AppDatabase.getDatabase(context)
    private val blockDao = database.blockDao()

    suspend fun blockUserLocal(uid: String, blockedUid: String) {
        val block = BlockEntity(uid = uid, blockedUid = blockedUid)
        blockDao.insertBlock(block)
    }

    suspend fun deleteBlockUserLocal(uid: String, blockedUid: String) {
        blockDao.deleteBlock(uid, blockedUid)
    }

    suspend fun isBlockedLocal(uid: String, blockedUid: String): Boolean {
        return blockDao.getBlock(uid, blockedUid) != null
    }
}
