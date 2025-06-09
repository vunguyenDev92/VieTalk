package com.android.internship.domain.usecase

import com.android.internship.domain.repository.AuthRepository
import com.android.internship.domain.repository.RoomRepository
import javax.inject.Inject

class StopTypingUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val roomRepository: RoomRepository,
) {
    operator fun invoke(rid: String) {
        val uid = authRepository.getCurrentUserId()
        val pastTime = "0"
        uid?.let {
            roomRepository.addTyping(rid, it, pastTime)
        }
    }
}
