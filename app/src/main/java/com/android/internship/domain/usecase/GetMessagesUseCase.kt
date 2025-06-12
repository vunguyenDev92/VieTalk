package com.android.internship.domain.usecase

import android.util.Log
import com.android.internship.data.model.Message
import com.android.internship.domain.repository.MessageRepository

class GetMessagesUseCase(
    private val repository: MessageRepository,
) {
    suspend operator fun invoke(rid: String, startMessageId: String? = null, limit: Int = 20): List<Message>? {
        return try {
            val remoteMessages = repository.getRemoteMessages(rid, startMessageId, limit)
            val localMessages = repository.getLocalMessages(rid)

            when {
                remoteMessages != null && remoteMessages.isNotEmpty() -> {
                    repository.saveLocalMessages(remoteMessages)

                    if (startMessageId == null && !localMessages.isNullOrEmpty()) {
                        val allMessages = (remoteMessages + localMessages)
                            .distinctBy { it.mid }
                            .sortedByDescending { it.time.toLongOrNull() ?: 0L }
                            .take(limit)

                        Log.d("GetMessagesUseCase", "Merged messages: ${allMessages.size}")
                        allMessages
                    } else {
                        remoteMessages
                    }
                }

                !localMessages.isNullOrEmpty() -> {
                    Log.d("GetMessagesUseCase", "Using local messages")

                    if (startMessageId == null) {
                        localMessages.take(limit)
                    } else {
                        val startIndex = localMessages.indexOfFirst { it.mid == startMessageId }
                        if (startIndex >= 0 && startIndex < localMessages.size - 1) {
                            localMessages.drop(startIndex + 1).take(limit)
                        } else {
                            emptyList()
                        }
                    }
                }

                else -> {
                    Log.d("GetMessagesUseCase", "No messages found")
                    emptyList()
                }
            }
        } catch (e: Exception) {
            Log.e("GetMessagesUseCase", "Error getting messages: ${e.message}", e)

            try {
                val localMessages = repository.getLocalMessages(rid)
                if (!localMessages.isNullOrEmpty()) {
                    Log.d("GetMessagesUseCase", "Fallback to local messages: ${localMessages.size}")
                    if (startMessageId == null) {
                        localMessages.take(limit)
                    } else {
                        val startIndex = localMessages.indexOfFirst { it.mid == startMessageId }
                        if (startIndex >= 0 && startIndex < localMessages.size - 1) {
                            localMessages.drop(startIndex + 1).take(limit)
                        } else {
                            emptyList()
                        }
                    }
                } else {
                    emptyList()
                }
            } catch (localError: Exception) {
                Log.e("GetMessagesUseCase", "Error getting local messages: ${localError.message}", localError)
                emptyList()
            }
        }
    }
}
