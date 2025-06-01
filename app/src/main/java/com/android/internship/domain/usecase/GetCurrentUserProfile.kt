package com.android.internship.domain.usecase

import com.android.internship.domain.repository.AuthRepository

class GetCurrentUserProfile(private val repository: AuthRepository) {
    operator fun invoke(): String? {
        // TODO: Get current user profile
        return null
    }
}
