package com.android.internship.domain.usecase

import com.android.internship.domain.repository.AuthRepository
import com.android.internship.domain.repository.UserRepository

class SignUpUseCase(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
) {
    suspend operator fun invoke(email: String, password: String): Result<String> {
        val result = authRepository.signUp(email, password)
        if (result.isSuccess) {
            authRepository.signIn(email, password)
            userRepository.addUserRemote(
                uid = authRepository.getCurrentUserId() ?: "",
                username = email.substringBefore("@"),
            )
        }
        return result
    }
}
