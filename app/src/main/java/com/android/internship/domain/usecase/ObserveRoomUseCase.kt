package com.android.internship.domain.usecase

import com.android.internship.data.model.Room
import com.android.internship.domain.repository.RoomRepository
import kotlinx.coroutines.flow.Flow

class ObserveRoomUseCase(private val repository: RoomRepository) {
    operator fun invoke(): Flow<List<Room>> {
        return repository.observeRooms()
    }
}
