package com.android.internship.domain.usecase

import com.android.internship.data.model.User
import com.android.internship.domain.repository.UserRepository
import javax.inject.Inject

class GetAllUsersInfoUseCase @Inject constructor(
    private val repository: UserRepository,
) {
    suspend operator fun invoke(): List<User> {
        val remoteUsers = repository.getAllUserRemote()
        val localUsers = repository.getAllUserLocal()

        return if (remoteUsers != null) {
            if (localUsers == null) {
                repository.saveLocalUsers(remoteUsers)
                remoteUsers
            } else {
                remoteUsers
            }
        } else {
            localUsers ?: emptyList()
        }
    }
}
