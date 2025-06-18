package com.android.internship.domain.usecase

import com.android.internship.data.model.Message
import com.android.internship.domain.repository.MessageRepository
import kotlinx.coroutines.flow.Flow

class ObserveNewMessagesUseCase(
    private val repository: MessageRepository,
) {
    operator fun invoke(roomId: String, afterTimestamp: Long): Flow<List<Message>> {
        return repository.observeNewMessages(roomId, afterTimestamp)
    }
}
