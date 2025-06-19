package com.android.internship.domain.usecase

import com.android.internship.domain.repository.AuthRepository

class GetCurrentUserIdUseCase(
    private val authRepository: AuthRepository,
) {

    operator fun invoke(): String? {
        return authRepository.getCurrentUserId()
    }
}
