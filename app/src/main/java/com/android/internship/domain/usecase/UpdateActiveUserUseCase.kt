package com.android.internship.domain.usecase

import com.android.internship.domain.repository.AuthRepository

class UpdateActiveUserUseCase(
    private val authRepository: AuthRepository,
) {
    operator fun invoke() {
        val time = System.currentTimeMillis().toString()
        val uid = authRepository.getCurrentUserId()
        uid?.let { authRepository.updateActiveUser(uid, time) }
    }
}
