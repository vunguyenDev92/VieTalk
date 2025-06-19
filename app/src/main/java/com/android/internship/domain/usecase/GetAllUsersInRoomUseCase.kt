package com.android.internship.domain.usecase

import com.android.internship.data.model.User
import com.android.internship.domain.repository.UserRepository

class GetAllUsersInRoomUseCase(
    private val userRepository: UserRepository,
) {
    suspend operator fun invoke(uids: List<String>): List<User> {
        if (uids.isEmpty()) {
            return emptyList()
        }
        return userRepository.getUsersInfo(uids)
    }
}
