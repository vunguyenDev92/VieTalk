package com.android.internship.domain.usecase

import com.android.internship.domain.repository.RoomRepository

class GetTypingUsersUseCase(
    private val roomRepository: RoomRepository,
) {
    suspend operator fun invoke(rid: String): List<String> {
        return roomRepository.getTypingUsers(rid)
    }
}
