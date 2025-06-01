package com.android.internship.domain.usecase

import com.android.internship.data.model.SignInResponse
import com.android.internship.domain.repository.AuthRepository

class SignInUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(username: String, password: String): SignInResponse {
        TODO("Sign in use case")
    }
}
