package com.android.internship.domain.usecase

import com.android.internship.domain.repository.AuthRepository

class GetSignInStatus(
    private val repository: AuthRepository,
) {
    operator fun invoke() = repository.isSignedIn()
}
