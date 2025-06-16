package com.android.internship.domain.usecase

import com.android.internship.data.model.UserRoom
import com.android.internship.domain.repository.RoomRepository
import com.android.internship.domain.repository.UserRoomRepository
import java.util.UUID

class CreateGroupUseCase(
    private val roomRepository: RoomRepository,
    private val userRoomRepository: UserRoomRepository,
) {
    operator fun invoke(roomName: String, userIds: List<String>): String {
        if (userIds.isEmpty()) {
            throw IllegalArgumentException("At least one other user ID is required to create a group.")
        }

        val rid = UUID.randomUUID().toString()

        roomRepository.addRoomRemote(
            rid = rid,
            isGroup = true,
            name = roomName,
        )

        userRoomRepository.addUserRoomsRemote(
            userRooms = userIds.map {
                UserRoom(
                    rid = rid,
                    uid = it,
                )
            },
        )

        return rid
    }
}
