package com.android.internship.data.datasource.local.entity

import androidx.room.Entity

@Entity(tableName = "blocks", primaryKeys = ["uid", "blockedUid"])
data class BlockEntity(
    val uid: String,
    val blockedUid: String,
)
