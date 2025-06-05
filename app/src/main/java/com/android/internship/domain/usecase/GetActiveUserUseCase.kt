package com.android.internship.domain.usecase

import com.android.internship.domain.repository.AuthRepository

class GetActiveUserUseCase(
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(uid: String): Boolean {
        return authRepository.getActiveUser(uid)
    }
}
