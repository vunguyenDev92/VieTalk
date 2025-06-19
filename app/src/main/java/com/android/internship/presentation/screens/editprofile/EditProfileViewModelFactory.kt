package com.android.internship.presentation.screens.editprofile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.android.internship.domain.repository.AuthRepository
import com.android.internship.domain.repository.ImageRepository
import com.android.internship.domain.repository.UserRepository
import com.android.internship.domain.usecase.GetCurrentUserIdUseCase
import com.android.internship.domain.usecase.GetCurrentUserProfileUseCase
import com.android.internship.domain.usecase.profile.UpdateUserProfileUseCase
import com.android.internship.domain.usecase.profile.UploadAvatarUseCase

class EditProfileViewModelFactory(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val imageRepository: ImageRepository,
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EditProfileViewModel::class.java)) {
            return EditProfileViewModel(
                getCurrentUserIdUseCase = GetCurrentUserIdUseCase(authRepository),
                getCurrentUserProfileUseCase = GetCurrentUserProfileUseCase(userRepository),
                updateUserProfileUseCase = UpdateUserProfileUseCase(userRepository),
                uploadAvatarUseCase = UploadAvatarUseCase(imageRepository),
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
