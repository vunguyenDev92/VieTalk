package com.android.internship.domain.usecase

import com.android.internship.domain.repository.AuthRepository
import com.android.internship.domain.repository.UserRoomRepository

class SeenMessageUseCase(
    private val authRepository: AuthRepository,
    private val userRoomRepository: UserRoomRepository,
) {
    operator fun invoke(rid: String, lastSeenMessageId: String) {
        val uid = authRepository.getCurrentUserId()

        uid?.let {
            userRoomRepository.updateLastSeenMessages(rid, uid, lastSeenMessageId)
        }
    }
}
