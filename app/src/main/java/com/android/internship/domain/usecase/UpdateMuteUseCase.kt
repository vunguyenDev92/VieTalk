package com.android.internship.domain.usecase

import com.android.internship.domain.repository.AuthRepository
import com.android.internship.domain.repository.UserRoomRepository

class UpdateMuteUseCase(
    private val authRepository: AuthRepository,
    private val userRoomRepository: UserRoomRepository,
) {
    operator fun invoke(rid: String, mute: Boolean, turnOnTime: String? = null) {
        authRepository.getCurrentUserId()?.let { uid ->
            userRoomRepository.updateMute(rid, uid, mute, turnOnTime)
        }
    }
}
