package com.android.internship.domain.usecase

import com.android.internship.data.model.Message
import com.android.internship.domain.repository.RoomRepository

class GetMessagesForRoomUseCase(
    private val roomRepository: RoomRepository,
) {
    suspend operator fun invoke(rid: String, startMessageId: String, limit: Int): List<Message> {
        return roomRepository.getMessagesForRoom(rid, startMessageId, limit)
    }
}
