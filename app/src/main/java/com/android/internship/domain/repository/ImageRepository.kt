package com.android.internship.domain.repository

interface ImageRepository {
    suspend fun uploadAvatar(imageUri: String, userId: String): String
}
