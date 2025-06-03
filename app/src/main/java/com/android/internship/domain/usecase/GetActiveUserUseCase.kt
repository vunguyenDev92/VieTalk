package com.android.internship.domain.usecase

import com.android.internship.data.model.User
import com.android.internship.domain.repository.AuthRepository

class GetActiveUserUseCase(
    private val authRepository: AuthRepository,
) {
    operator fun invoke(): User {
        TODO("Provide the return value")
    }
}
