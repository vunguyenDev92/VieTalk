package com.android.internship.domain.usecase

import com.android.internship.domain.repository.AuthRepository
import com.android.internship.domain.repository.RoomRepository
import javax.inject.Inject

class AddTypingUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val roomRepository: RoomRepository,
) {
    operator fun invoke(rid: String, isTyping: Boolean) {
        val uid = authRepository.getCurrentUserId()
        val time = if (isTyping) System.currentTimeMillis().toString() else "0"

        uid?.let {
            roomRepository.addTyping(rid, it, time)
        }
    }
}
