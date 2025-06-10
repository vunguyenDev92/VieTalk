package com.android.internship.domain.usecase

import com.android.internship.domain.repository.AuthRepository
import com.android.internship.domain.repository.MessageRepository
import java.util.UUID

class SendMessagesUseCase(
    private val authRepository: AuthRepository,
    private val messageRepository: MessageRepository,
) {
    operator fun invoke(content: String, rid: String) {
        val uid = authRepository.getCurrentUserId()
        val mid = UUID.randomUUID().toString()

        uid?.let {
            messageRepository.addRemoteMessage(
                mid = mid,
                uid = uid,
                rid = rid,
                content = content,
                timestamp = System.currentTimeMillis().toString(),
            )
        }
    }
}
