package com.android.internship.domain.usecase

import com.android.internship.data.model.Room
import com.android.internship.domain.repository.RoomRepository
import kotlinx.coroutines.flow.Flow

class ObserveSingleRoomUseCase(private val repository: RoomRepository) {
    operator fun invoke(rid: String): Flow<Room?> {
        return repository.observeRoom(rid)
    }
}
