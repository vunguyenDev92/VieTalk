package com.android.internship.domain.usecase

import com.android.internship.data.model.Room
import com.android.internship.domain.repository.RoomRepository

class GetRoomUseCase(
    private val roomRepository: RoomRepository,
) {
    operator fun invoke(): Room {
        TODO("Provide the return value")
    }
}
