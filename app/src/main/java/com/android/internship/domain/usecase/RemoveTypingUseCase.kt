package com.android.internship.domain.usecase

import com.android.internship.data.model.Room
import com.android.internship.domain.repository.RoomRepository

class RemoveTypingUseCase(
    private val roomRepository: RoomRepository,
) {
    suspend operator fun invoke(uid: String, rid: String): Room? {
        roomRepository.removeTyping(rid, uid)
        return roomRepository.getRoomRemote(rid)
    }
}
