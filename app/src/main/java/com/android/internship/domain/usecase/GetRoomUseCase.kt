package com.android.internship.domain.usecase

import com.android.internship.data.model.Room
import com.android.internship.domain.repository.RoomRepository

class GetRoomUseCase(
    private val roomRepository: RoomRepository,
) {
    suspend operator fun invoke(rid: String): Room? {
        val roomRemote = roomRepository.getRoomRemote(rid)
        val roomLocal = roomRepository.getRoomLocal(rid)

        return if (roomRemote != null) {
            if (roomLocal == null) {
                roomRepository.saveLocalRoom(roomRemote)
                roomRemote
            }
            roomRemote
        } else {
            roomLocal
        }
    }
}
