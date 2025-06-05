package com.android.internship.domain.usecase

import com.android.internship.data.model.Message
import com.android.internship.domain.repository.RoomRepository

class GetMessagesUseCase(
    private val roomRepository: RoomRepository,
) {
    suspend operator fun invoke(rid: String, startMessageId: String? = null, limit: Int = 20): List<Message>? {
        val remoteMessages = roomRepository.getRemoteMessages(rid, startMessageId, limit)
        val localMessages = roomRepository.getLocalMessages(rid)

        return if (remoteMessages != null) {
            if (localMessages == null || (startMessageId == null && remoteMessages.first().mid != localMessages.first().mid)) {
                roomRepository.saveLocalMessages(remoteMessages)
                remoteMessages
            } else {
                remoteMessages
            }
        } else {
            localMessages
        }
    }
}
