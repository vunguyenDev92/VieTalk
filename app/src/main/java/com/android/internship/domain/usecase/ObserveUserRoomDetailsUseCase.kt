package com.android.internship.domain.usecase

import com.android.internship.data.model.UserRoom
import com.android.internship.domain.repository.RoomRepository
import com.android.internship.domain.repository.UserRoomRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class ObserveUserRoomDetailsUseCase @Inject constructor(
    private val repository: UserRoomRepository,
) {
    operator fun invoke(rid: String): Flow<List<UserRoom>> {
        return repository.observeUserRoomDetails(rid)
    }
}
