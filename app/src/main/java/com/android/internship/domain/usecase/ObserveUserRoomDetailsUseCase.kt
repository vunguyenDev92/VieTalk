package com.android.internship.domain.usecase

import com.android.internship.data.model.UserRoom
import com.android.internship.domain.repository.RoomRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class ObserveUserRoomDetailsUseCase @Inject constructor(
    private val roomRepository: RoomRepository,
) {
    operator fun invoke(rid: String): Flow<List<UserRoom>> {
        return roomRepository.observeUserRoomDetails(rid)
    }
}
