package com.android.internship.presentation.screens.editprofile

import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.internship.domain.usecase.GetCurrentUserIdUseCase
import com.android.internship.domain.usecase.GetCurrentUserProfileUseCase
import com.android.internship.domain.usecase.profile.UpdateUserProfileUseCase
import com.android.internship.domain.usecase.profile.UploadAvatarUseCase
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EditProfileViewModel(
    private val getCurrentUserIdUseCase: GetCurrentUserIdUseCase,
    private val getCurrentUserProfileUseCase: GetCurrentUserProfileUseCase,
    private val updateUserProfileUseCase: UpdateUserProfileUseCase,
    private val uploadAvatarUseCase: UploadAvatarUseCase,
) : ViewModel() {

    private val currentUserId: String = checkNotNull(getCurrentUserIdUseCase())
    private val _uiState = MutableStateFlow(EditProfileState())
    val uiState = _uiState.asStateFlow()

    init {
        loadCurrentUserProfile()
    }

    private fun loadCurrentUserProfile() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val user = getCurrentUserProfileUseCase(currentUserId)
                val userEmail = FirebaseAuth.getInstance().currentUser?.email ?: "No email"
                if (user != null) {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            currentUser = user,
                            userName = TextFieldValue(user.username),
                            userEmail = userEmail,
                            userAvatarUrl = user.avatar,
                        )
                    }
                } else {
                    _uiState.update {
                        it.copy(isLoading = false, errorMessage = "Could not load user profile.", showDialog = true)
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(isLoading = false, errorMessage = e.message ?: "An unknown error occurred.", showDialog = true)
                }
            }
        }
    }

    fun onNameChange(textFieldValue: TextFieldValue) {
        _uiState.update { it.copy(userName = textFieldValue) }
    }

    fun uploadAvatar(imageUri: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isUploadingAvatar = true) }
            try {
                val avatarUrl = uploadAvatarUseCase(imageUri, currentUserId)
                _uiState.update {
                    it.copy(
                        isUploadingAvatar = false,
                        userAvatarUrl = avatarUrl,
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isUploadingAvatar = false,
                        errorMessage = e.message ?: "An unknown error occurred during upload.",
                        showDialog = true,
                    )
                }
            }
        }
    }

    fun saveProfile() {
        val name = _uiState.value.userName.text.trim()
        if (name.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Name cannot be empty.", showDialog = true) }
            return
        }
        if (name.length < 2) {
            _uiState.update { it.copy(errorMessage = "Name must be at least 2 characters.", showDialog = true) }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true) }
            try {
                updateUserProfileUseCase(
                    uid = currentUserId,
                    newUsername = name,
                    newAvatarUrl = _uiState.value.userAvatarUrl,
                )
                _uiState.update { currentState ->
                    val updatedUser = currentState.currentUser?.copy(username = name, avatar = _uiState.value.userAvatarUrl)
                    currentState.copy(
                        isSaving = false,
                        currentUser = updatedUser,
                        successMessage = "Profile updated successfully!",
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isSaving = false,
                        errorMessage = e.message ?: "An unknown error occurred while saving.",
                        showDialog = true,
                    )
                }
            }
        }
    }

    fun dismissDialog() {
        _uiState.update { it.copy(showDialog = false, errorMessage = null) }
    }

    fun clearSuccessMessage() {
        _uiState.update { it.copy(successMessage = null) }
    }

    fun isDataChanged(): Boolean {
        val state = _uiState.value
        val originalUser = state.currentUser
        return originalUser?.let {
            state.userName.text.trim() != it.username || state.userAvatarUrl != it.avatar
        } ?: (state.userName.text.isNotBlank() || state.userAvatarUrl != null)
    }
}
