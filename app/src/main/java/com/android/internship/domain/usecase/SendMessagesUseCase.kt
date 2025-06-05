package com.android.internship.domain.usecase

import com.android.internship.data.model.Message
import com.android.internship.domain.repository.AuthRepository
import com.android.internship.domain.repository.RoomRepository
import java.util.UUID

class SendMessagesUseCase(
    private val authRepository: AuthRepository,
    private val roomRepository: RoomRepository,
) {
    operator fun invoke(content: String, rid: String) {
        val uid = authRepository.getCurrentUserId()
        val mid = UUID.randomUUID().toString()

        uid?.let {
            val message = Message(
                mid = mid,
                uid = uid,
                rid = rid,
                content = content,
                time = System.currentTimeMillis().toString(),
            )

            roomRepository.addRemoteMessage(
                rid = rid,
                message = message,
            )
        }
    }
}
