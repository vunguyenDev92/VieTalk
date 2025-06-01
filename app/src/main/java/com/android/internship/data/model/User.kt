package com.android.internship.data.model

data class User(
    val uid: String,
    val username: String,
    val email: String,
    val active: Boolean,
    val avatar: String,
    val block: List<String>,
    val userRooms: List<UserRoom>,
)
