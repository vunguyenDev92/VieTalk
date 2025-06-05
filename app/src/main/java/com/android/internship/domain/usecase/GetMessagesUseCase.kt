package com.android.internship.domain.usecase

import com.android.internship.data.model.Message
import com.android.internship.domain.repository.RoomRepository

class GetMessagesUseCase(
    private val roomRepository: RoomRepository,
) {
    suspend operator fun invoke(rid: String, startMessageId: String? = null, limit: Int = 20): List<Message>? {
        val remoteMessages = roomRepository.getRemoteMessages(rid, startMessageId, limit)

        return remoteMessages ?: roomRepository.getLocalMessages(rid)
    }
}
