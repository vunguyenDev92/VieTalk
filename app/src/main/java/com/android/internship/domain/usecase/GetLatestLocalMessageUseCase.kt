package com.android.internship.domain.usecase

import com.android.internship.data.model.Message
import com.android.internship.domain.repository.MessageRepository

class GetLatestLocalMessageUseCase(
    private val repository: MessageRepository,
) {
    suspend operator fun invoke(rid: String): Message? {
        return repository.getLatestLocalMessage(rid)
    }
}
