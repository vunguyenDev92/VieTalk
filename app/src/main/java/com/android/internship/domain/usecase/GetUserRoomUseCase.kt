package com.android.internship.domain.usecase

import com.android.internship.data.model.UserRoom
import com.android.internship.domain.repository.RoomRepository

class GetUserRoomUseCase(
    private val roomRepository: RoomRepository,
) {
    suspend operator fun invoke(rid: String): List<UserRoom> {
        val userRoomsRemote = roomRepository.getUserRoomRemote(rid)
        val userRoomsLocal = roomRepository.getUserRoomLocal(rid)

        return if (userRoomsRemote.isNotEmpty()) {
            if (userRoomsLocal == null) {
                roomRepository.saveLocalUserRoom(userRoomsRemote)
                userRoomsRemote
            } else {
                userRoomsRemote
            }
        } else {
            userRoomsLocal ?: emptyList()
        }
    }
}
