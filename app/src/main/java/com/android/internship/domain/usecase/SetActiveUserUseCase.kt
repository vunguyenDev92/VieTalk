package com.android.internship.domain.usecase

import com.android.internship.domain.repository.AuthRepository

class SetActiveUserUseCase(
    private val authRepository: AuthRepository,
) {
    operator fun invoke(isActive: Boolean) {
        val uid = authRepository.getCurrentUserId()
        uid?.let { authRepository.setActiveUser(uid, isActive) }
    }
}
