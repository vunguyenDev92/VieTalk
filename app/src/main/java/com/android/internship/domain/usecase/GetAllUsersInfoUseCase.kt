package com.android.internship.domain.usecase

import com.android.internship.data.model.User
import com.android.internship.domain.repository.AuthRepository
import javax.inject.Inject

class GetAllUsersInfoUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(uids: List<String>): List<User> {
        if (uids.isEmpty()) {
            return emptyList()
        }
        return authRepository.getUsersInfo(uids)
    }
}
