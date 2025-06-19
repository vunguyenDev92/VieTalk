package com.android.internship.data.repository

import com.android.internship.data.datasource.remote.ImageUploadDataSource
import com.android.internship.domain.repository.ImageRepository
import com.android.internship.presentation.components.utils.IConnectivityObserver
import kotlinx.coroutines.flow.first

class ImageRepositoryImpl(
    private val imageUploadDataSource: ImageUploadDataSource,
    private val connectivityObserver: IConnectivityObserver,
) : ImageRepository {

    override suspend fun uploadAvatar(imageUri: String, userId: String): String {
        if (connectivityObserver.observe().first() != IConnectivityObserver.Status.Available) {
            throw Exception("No internet connection.")
        }
        val publicId = "avatars/user_$userId"
        return imageUploadDataSource.uploadAvatar(imageUri, publicId)
    }
}
