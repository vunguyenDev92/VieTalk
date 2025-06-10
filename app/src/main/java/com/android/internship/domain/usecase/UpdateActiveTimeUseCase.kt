package com.android.internship.domain.usecase

import com.android.internship.domain.repository.AuthRepository
import com.android.internship.domain.repository.UserRepository

class UpdateActiveTimeUseCase(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
) {
    operator fun invoke() {
        val time = System.currentTimeMillis().toString()
        val uid = authRepository.getCurrentUserId()
        uid?.let { userRepository.updateActiveTime(uid, time) }
    }
}
