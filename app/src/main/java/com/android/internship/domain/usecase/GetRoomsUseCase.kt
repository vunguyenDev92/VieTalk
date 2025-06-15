package com.android.internship.domain.usecase

import com.android.internship.data.model.Room
import com.android.internship.domain.repository.RoomRepository

class GetRoomsUseCase(
    private val repository: RoomRepository,
) {
    suspend operator fun invoke(rids: List<String>): List<Room>? {
        val roomRemote = repository.getRoomsRemote(rids)
        val roomLocal = repository.getRoomsLocal(rids)

        return if (roomRemote != null) {
            if (roomLocal == null) {
                repository.saveRoomsLocal(roomRemote)
                roomRemote
            }
            roomRemote
        } else {
            roomLocal
        }
    }
}
