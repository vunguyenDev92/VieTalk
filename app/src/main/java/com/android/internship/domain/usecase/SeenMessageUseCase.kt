package com.android.internship.domain.usecase

import com.android.internship.domain.repository.AuthRepository
import com.android.internship.domain.repository.RoomRepository

class SeenMessageUseCase(
    private val authRepository: AuthRepository,
    private val roomRepository: RoomRepository,
) {
    suspend operator fun invoke(rid: String, mid: String) {
        val uid = authRepository.getCurrentUserId()
        roomRepository.seenMessage(rid, uid, mid)
    }
}
