package com.android.internship.domain.usecase

import com.android.internship.domain.repository.UserRoomRepository

class UpdateBlockUseCase(
    private val userRoomRepository: UserRoomRepository,
) {
    operator fun invoke(rid: String, uid: String, isBlocked: Boolean) {
        userRoomRepository.updateBlock(rid, uid, isBlocked)
    }
}
