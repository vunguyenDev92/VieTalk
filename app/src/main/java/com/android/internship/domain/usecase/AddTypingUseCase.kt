package com.android.internship.domain.usecase

import com.android.internship.domain.repository.AuthRepository
import com.android.internship.domain.repository.RoomRepository

class AddTypingUseCase(
    private val authRepository: AuthRepository,
    private val roomRepository: RoomRepository,
) {
    operator fun invoke(rid: String) {
        val uid = authRepository.getCurrentUserId()
        uid?.let {
            roomRepository.addTyping(rid, uid)
        }
    }
}
