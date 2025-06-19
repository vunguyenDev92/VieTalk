package com.android.internship.presentation.screens.editprofile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.android.internship.di.AppContainer
import com.android.internship.domain.usecase.GetCurrentUserIdUseCase
import com.android.internship.domain.usecase.GetCurrentUserProfileUseCase
import com.android.internship.domain.usecase.UpdateUserProfileUseCase
import com.android.internship.domain.usecase.UploadAvatarUseCase

class EditProfileViewModelFactory(private val appContainer: AppContainer) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EditProfileViewModel::class.java)) {
            return EditProfileViewModel(
                getCurrentUserIdUseCase = GetCurrentUserIdUseCase(appContainer.authRepository),
                getCurrentUserProfileUseCase = GetCurrentUserProfileUseCase(appContainer.userRepository),
                updateUserProfileUseCase = UpdateUserProfileUseCase(appContainer.userRepository),
                uploadAvatarUseCase = UploadAvatarUseCase(appContainer.imageRepository),
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
