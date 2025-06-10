package com.android.internship.domain.usecase

import com.android.internship.domain.repository.AuthRepository
import com.android.internship.domain.repository.UserRoomRepository

class UpdateTypingTimeUseCase(
    private val authRepository: AuthRepository,
    private val userRoomRepository: UserRoomRepository,
) {
    operator fun invoke(rid: String) {
        val uid = authRepository.getCurrentUserId()
        val time = System.currentTimeMillis().toString()

        uid?.let {
            userRoomRepository.updateTypingTime(rid, uid, time)
        }
    }
}
