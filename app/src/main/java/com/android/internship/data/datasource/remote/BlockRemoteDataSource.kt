package com.android.internship.data.datasource.remote

import com.android.internship.data.model.Block
import com.google.firebase.firestore.FirebaseFirestore

class BlockRemoteDataSource {
    private val firestore = FirebaseFirestore.getInstance()
    private val blocksCollection = firestore.collection("blocks")

    fun blockUserRemote(uid: String, blockedUid: String) {
        val block = Block(uid = uid, blockedUid = blockedUid)
        blocksCollection.document(uid).set(block)
    }

    fun unblockUserRemote(uid: String, blockedUid: String) {
        blocksCollection.document(uid).delete()
    }

    fun isBlockedRemote(uid: String, blockedUid: String): Boolean {
        var isBlocked = false
        blocksCollection.document(uid).get().addOnSuccessListener { document ->
            if (document != null && document.exists()) {
                val block = document.toObject(Block::class.java)
                if (block != null && block.blockedUid == blockedUid) {
                    isBlocked = true
                }
            }
        }
        return isBlocked
    }
}
