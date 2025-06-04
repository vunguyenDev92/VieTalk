package com.android.internship.domain.usecase

import com.android.internship.data.model.Message
import com.android.internship.data.model.Room
import com.android.internship.domain.repository.AuthRepository
import com.android.internship.domain.repository.RoomRepository
import java.util.UUID

class SendMessagesUseCase(
    private val authRepository: AuthRepository,
    private val roomRepository: RoomRepository,
) {
    suspend operator fun invoke(content: String, rid: String): Room? {
        roomRepository.addRemoteMessage(
            rid = rid,
            message = Message(
                mid = UUID.randomUUID().toString(),
                uid = authRepository.getCurrentUserId(),
                content = content,
                time = System.currentTimeMillis().toString(),
            ),
        )
        return roomRepository.getRoomRemote(rid)
    }
}
