package com.android.internship.domain.usecase

import com.android.internship.data.model.User
import com.android.internship.domain.repository.UserRepository

class GetAllUsersInfoUseCase(
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
