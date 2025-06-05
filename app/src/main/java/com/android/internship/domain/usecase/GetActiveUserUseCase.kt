package com.android.internship.domain.usecase

import com.android.internship.domain.repository.AuthRepository

class GetActiveUserUseCase(
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(uid: String): Boolean {
        val currentTime = System.currentTimeMillis()
        val lastActiveTime = authRepository.getLastActiveTimeUser(uid)?.toLongOrNull() ?: 0L
        return currentTime - lastActiveTime <= DEFAULT_TIMEOUT_MS
    }

    companion object {
        private const val DEFAULT_TIMEOUT_MS = 5 * 60 * 1000
    }
}
