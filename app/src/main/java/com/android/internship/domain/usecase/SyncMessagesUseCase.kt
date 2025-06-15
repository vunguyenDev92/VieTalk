package com.android.internship.domain.usecase

import com.android.internship.data.datasource.local.MessageLocalDataSource
import com.android.internship.domain.repository.MessageRepository
import kotlinx.coroutines.flow.first

class SyncMessagesUseCase(private val repository: MessageRepository) {
    suspend operator fun invoke(rid: String) {
        val initialMessages = repository.observeMessages(rid).first()
        if (initialMessages.isEmpty()) {
            repository.fetchAndCacheInitialMessages(rid, MessageLocalDataSource.MESSAGE_CACHE_LIMIT)
        }
        repository.syncRemoteMessagesToLocal(rid)
    }
}
