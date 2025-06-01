package com.android.internship.domain.usecase

import com.android.internship.data.model.SignInResponse
import com.android.internship.domain.repository.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SignInUseCase(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String): SignInResponse {
        return withContext(Dispatchers.IO) {
            try {
                repository.signIn(email, password)
            } catch (e: Exception) {
                SignInResponse(
                    success = false,
                    message = e.message ?: "Sign-in failed",
                    user = null
                )
            }
        }
    }
}
