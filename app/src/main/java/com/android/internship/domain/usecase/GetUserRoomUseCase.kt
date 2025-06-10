package com.android.internship.domain.usecase

import com.android.internship.data.model.UserRoom
import com.android.internship.domain.repository.UserRoomRepository

class GetUserRoomUseCase(
    private val repository: UserRoomRepository,
) {
    suspend operator fun invoke(rid: String): List<UserRoom> {
        val userRoomsRemote = repository.getUserRoomRemote(rid)
        val userRoomsLocal = repository.getUserRoomLocal(rid)

        return if (userRoomsRemote.isNotEmpty()) {
            if (userRoomsLocal == null) {
                repository.saveLocalUserRoom(userRoomsRemote)
                userRoomsRemote
            } else {
                userRoomsRemote
            }
        } else {
            userRoomsLocal ?: emptyList()
        }
    }
}
