package com.android.internship.domain.usecase

import com.android.internship.data.model.Message
import com.android.internship.domain.repository.MessageRepository

class GetMessagesUseCase(
    private val repository: MessageRepository,
) {
    suspend operator fun invoke(rid: String, startMessageId: String? = null, limit: Int = 20): List<Message>? {
        val remoteMessages = repository.getRemoteMessages(rid, startMessageId, limit)
        val localMessages = repository.getLocalMessages(rid)

        return if (remoteMessages != null) {
            if (localMessages == null || (startMessageId == null && remoteMessages.first().mid != localMessages.first().mid)) {
                repository.saveLocalMessages(remoteMessages)
                remoteMessages
            } else {
                remoteMessages
            }
        } else {
            localMessages
        }
    }
}
