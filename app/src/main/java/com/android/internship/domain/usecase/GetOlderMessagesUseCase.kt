package com.android.internship.domain.usecase

import com.android.internship.domain.repository.MessageRepository

class GetOlderMessagesUseCase(
    private val repository: MessageRepository,
) {
    suspend operator fun invoke(rid: String, limit: Int): Boolean {
        return repository.fetchAndCacheOlderMessages(rid, limit)
    }
}
