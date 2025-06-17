package com.android.internship.domain.usecase

import com.android.internship.data.model.UserRoom
import com.android.internship.domain.repository.AuthRepository
import com.android.internship.domain.repository.UserRoomRepository
import kotlinx.coroutines.flow.Flow

class ObserveUserRoomForUserUseCase(
    private val userRoomRepository: UserRoomRepository,
    private val authRepository: AuthRepository,
) {
    operator fun invoke(): Flow<List<UserRoom>>? {
        val uid = authRepository.getCurrentUserId()
        return uid?.let {
            userRoomRepository.observeUserRoomForUserRemote(it)
        }
    }
}
