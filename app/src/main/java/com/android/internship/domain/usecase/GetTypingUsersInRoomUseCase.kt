package com.android.internship.domain.usecase

import com.android.internship.domain.repository.RoomRepository

class GetTypingUsersInRoomUseCase(
    private val roomRepository: RoomRepository,
) {
    suspend operator fun invoke(rid: String): List<String> {
        return roomRepository.getTypingUsersInRoom(rid)
    }
}
