package com.android.internship.domain.usecase

import com.android.internship.domain.repository.AuthRepository

class SetActiveUserUseCase(
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(isActive: Boolean) {
        val uid = authRepository.getCurrentUserId()
        authRepository.setActiveUser(uid, isActive)
    }
}
