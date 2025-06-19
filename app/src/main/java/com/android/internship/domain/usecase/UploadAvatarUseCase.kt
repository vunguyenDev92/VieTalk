package com.android.internship.domain.usecase

import com.android.internship.domain.repository.ImageRepository

class UploadAvatarUseCase(private val imageRepository: ImageRepository) {
    suspend operator fun invoke(imageUri: String, userId: String): String {
        return imageRepository.uploadAvatar(imageUri, userId)
    }
}
