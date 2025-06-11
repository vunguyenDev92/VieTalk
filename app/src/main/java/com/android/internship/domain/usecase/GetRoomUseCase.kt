package com.android.internship.domain.usecase

import com.android.internship.data.model.Room
import com.android.internship.domain.repository.RoomRepository

class GetRoomUseCase(
    private val repository: RoomRepository,
) {
    suspend operator fun invoke(rid: String): Room? {
        val roomRemote = repository.getRoomRemote(rid)
        val roomLocal = repository.getRoomLocal(rid)

        return if (roomRemote != null) {
            if (roomLocal == null) {
                repository.saveLocalRoom(roomRemote)
                roomRemote
            }
            roomRemote
        } else {
            roomLocal
        }
    }
}
