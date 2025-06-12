package com.android.internship.domain.usecase

import com.android.internship.domain.repository.AuthRepository

class LogoutUserCase(
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke() {
        authRepository.logout()
    }
}
