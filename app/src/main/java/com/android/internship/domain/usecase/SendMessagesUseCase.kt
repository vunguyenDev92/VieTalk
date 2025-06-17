// file: com/android/internship/domain/usecase/SendMessagesUseCase.kt

package com.android.internship.domain.usecase

import com.android.internship.data.model.Message
import com.android.internship.domain.repository.AuthRepository
import com.android.internship.domain.repository.MessageRepository
import com.android.internship.domain.repository.RoomRepository
import java.util.UUID

class SendMessagesUseCase(
    private val authRepository: AuthRepository,
    private val messageRepository: MessageRepository,
    private val roomRepository: RoomRepository,
) {
    operator fun invoke(
        content: String,
        rid: String,
        senderName: String,
        senderAvatar: String?,
    ) {
        val uid = authRepository.getCurrentUserId()
        val mid = UUID.randomUUID().toString()
        val timestamp = System.currentTimeMillis().toString()

        uid?.let {
            val newMessage = Message(
                mid = mid,
                uid = it,
                rid = rid,
                content = content,
                time = timestamp,
                senderName = senderName,
                senderAvatar = senderAvatar,
            )
            messageRepository.addRemoteMessageNew(mid, rid, it, content, timestamp, senderAvatar ?: "", senderName)

            roomRepository.updateLastMessage(rid, newMessage)
        }
    }
}
