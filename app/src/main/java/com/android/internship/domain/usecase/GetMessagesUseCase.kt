package com.android.internship.domain.usecase

import android.util.Log
import com.android.internship.data.model.Message
import com.android.internship.domain.repository.MessageRepository

class GetMessagesUseCase(
    private val repository: MessageRepository,
) {
    suspend operator fun invoke(rid: String, startMessageId: String? = null, limit: Int = 20): List<Message>? {
        return try {
            Log.d("GetMessagesUseCase", "Getting messages for room: $rid, startId: $startMessageId, limit: $limit")

            val remoteMessages = repository.getRemoteMessages(rid, startMessageId, limit)
            val localMessages = repository.getLocalMessages(rid)

            Log.d("GetMessagesUseCase", "Remote messages: ${remoteMessages?.size ?: 0}, Local messages: ${localMessages?.size ?: 0}")

            when {
                // Có remote messages
                remoteMessages != null && remoteMessages.isNotEmpty() -> {
                    Log.d("GetMessagesUseCase", "Using remote messages")

                    // Lưu remote messages vào local
                    repository.saveLocalMessages(remoteMessages)

                    // Nếu đang load trang đầu (startMessageId == null), merge với local
                    if (startMessageId == null && !localMessages.isNullOrEmpty()) {
                        // Merge và loại bỏ duplicate
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

                // Không có remote, dùng local
                !localMessages.isNullOrEmpty() -> {
                    Log.d("GetMessagesUseCase", "Using local messages")

                    if (startMessageId == null) {
                        // Load trang đầu từ local
                        localMessages.take(limit)
                    } else {
                        // Load pagination từ local
                        val startIndex = localMessages.indexOfFirst { it.mid == startMessageId }
                        if (startIndex >= 0 && startIndex < localMessages.size - 1) {
                            localMessages.drop(startIndex + 1).take(limit)
                        } else {
                            emptyList()
                        }
                    }
                }

                // Không có messages nào
                else -> {
                    Log.d("GetMessagesUseCase", "No messages found")
                    emptyList()
                }
            }
        } catch (e: Exception) {
            Log.e("GetMessagesUseCase", "Error getting messages: ${e.message}", e)

            // Fallback to local messages nếu có lỗi
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
