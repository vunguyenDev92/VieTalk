package com.android.internship.domain.usecase.profile

import com.android.internship.domain.repository.UserRepository

class UpdateUserProfileUseCase(private val userRepository: UserRepository) {
    suspend operator fun invoke(uid: String, newUsername: String, newAvatarUrl: String?) {
        userRepository.updateUserProfile(uid, newUsername, newAvatarUrl)
    }
}
