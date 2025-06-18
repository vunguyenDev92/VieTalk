package com.android.internship.domain.usecase

import com.android.internship.data.model.Message
import com.android.internship.domain.repository.MessageRepository

class SaveLocalMessagesUseCase(
    private val repository: MessageRepository,
) {
    suspend operator fun invoke(messages: List<Message>) {
        repository.saveLocalMessages(messages)
    }
}
