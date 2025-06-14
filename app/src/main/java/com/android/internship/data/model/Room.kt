package com.android.internship.data.model

data class Room(
    @JvmField
    val rid: String = "",
    @JvmField
    val isGroup: Boolean = false,
    val avatar: String? = null,
    val name: String? = null,
)
