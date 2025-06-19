package com.android.internship.domain.usecase

import com.android.internship.data.model.User
import com.android.internship.domain.repository.UserRepository

class GetCurrentUserProfileUseCase(private val userRepository: UserRepository) {
    suspend operator fun invoke(uid: String): User? {
        return userRepository.getUserRemote(uid) ?: userRepository.getUserLocal(uid)
    }
}
