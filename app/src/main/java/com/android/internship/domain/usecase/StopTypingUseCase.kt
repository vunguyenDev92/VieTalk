package com.android.internship.domain.usecase

import com.android.internship.domain.repository.AuthRepository
import com.android.internship.domain.repository.UserRoomRepository
import javax.inject.Inject

class StopTypingUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRoomRepository: UserRoomRepository,
) {
    operator fun invoke(rid: String) {
        val uid = authRepository.getCurrentUserId()
        val pastTime = "0"
        uid?.let {
            userRoomRepository.updateTypingTime(rid, it, pastTime)
        }
    }
}
