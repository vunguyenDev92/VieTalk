package com.android.internship.domain.usecase

import com.android.internship.data.model.UserRoom
import com.android.internship.domain.repository.AuthRepository
import com.android.internship.domain.repository.UserRoomRepository

class GetUserRoomForUserUseCase(
    private val userRoomRepository: UserRoomRepository,
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(): List<UserRoom> {
        val uid = authRepository.getCurrentUserId()
        uid?.let {
            val roomRemote = userRoomRepository.getUserRoomForUserRemote(it)
            val roomLocal = userRoomRepository.getUserRoomForUserLocal(it)
            return if (roomRemote.isNotEmpty()) {
                userRoomRepository.saveLocalUserRoom(roomRemote)
                roomRemote
            } else {
                roomLocal ?: emptyList()
            }
        }
        return emptyList()
    }
}
