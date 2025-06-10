package com.android.internship.domain.usecase

import com.android.internship.domain.repository.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SignInUseCase(
    private val repository: AuthRepository,
) {
    suspend operator fun invoke(email: String, password: String): Result<String> {
        return withContext(Dispatchers.IO) {
            repository.signIn(email, password)
        }
    }
}
