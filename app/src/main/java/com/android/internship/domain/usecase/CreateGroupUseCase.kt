package com.android.internship.domain.usecase

import com.android.internship.domain.repository.RoomRepository
import com.android.internship.domain.repository.UserRoomRepository
import java.util.UUID

class CreateGroupUseCase(
    private val roomRepository: RoomRepository,
    private val userRoomRepository: UserRoomRepository,
) {
    operator fun invoke(roomName: String, userIds: List<String>): String {
        val rid = UUID.randomUUID().toString()

        roomRepository.addRoomRemote(
            rid = rid,
            isGroup = true,
            name = roomName,
        )

        userIds.forEach { userId ->
            userRoomRepository.addUserRoomRemote(
                rid = rid,
                uid = userId,
            )
        }

        return rid
    }
}
