package com.android.internship.domain.usecase

import com.android.internship.data.model.Message
import com.android.internship.domain.repository.AuthRepository
import com.android.internship.domain.repository.RoomRepository
import java.util.UUID

class SendMessagesUseCase(
    private val authRepository: AuthRepository,
    private val roomRepository: RoomRepository,
) {
    suspend operator fun invoke(content: String, rid: String): List<Message>? {
        val uid = authRepository.getCurrentUserId()
        val mid = UUID.randomUUID().toString()

        uid?.let {
            val message = Message(
                mid = mid,
                uid = uid,
                content = content,
                time = System.currentTimeMillis().toString(),
            )
            roomRepository.addRemoteMessage(
                rid = rid,
                message = message,
            )

            val localRoom = roomRepository.getRoomLocal(rid)

            localRoom?.let {
                val messages = it.messages.toMutableList()

                messages.add(message)

                roomRepository.saveLocalRoom(it.copy(messages = messages))
            }

            return localRoom?.messages
        }

        return null
    }
}
