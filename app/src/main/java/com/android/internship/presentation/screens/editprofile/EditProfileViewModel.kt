package com.android.internship.presentation.screens.editprofile

import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import com.android.internship.domain.usecase.GetCurrentUserIdUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class EditProfileViewModel(
    private val getCurrentUserIdUseCase: GetCurrentUserIdUseCase,
//    private val getCurrentUserUseCase: GetCurrentUserUseCase,
//    private val updateUserProfileUseCase: UpdateUserProfileUseCase,
//    private val uploadAvatarUseCase: UploadAvatarUseCase,
) : ViewModel() {

    private val currentUserId: String = checkNotNull(getCurrentUserIdUseCase())

    private val _uiState = MutableStateFlow(EditProfileState())
    val uiState = _uiState.asStateFlow()

    init {
        loadCurrentUserProfile()
    }

    private fun loadCurrentUserProfile() {
//        viewModelScope.launch {
//            _uiState.update { it.copy(isLoading = true) }
//            try {
//                val user = getCurrentUserUseCase(currentUserId)
//                if (user != null) {
//                    _uiState.update {
//                        it.copy(
//                            isLoading = false,
//                            currentUser = user,
//                            userName = TextFieldValue(user.username ?: ""),
//                            userEmail = user.email ?: "",
//                            userAvatarUrl = user.avatar ?: "",
//                        )
//                    }
//                } else {
//                    _uiState.update {
//                        it.copy(
//                            isLoading = false,
//                            errorMessage = "Không thể tải thông tin người dùng",
//                        )
//                    }
//                }
//            } catch (e: Exception) {
//                _uiState.update {
//                    it.copy(
//                        isLoading = false,
//                        errorMessage = "Lỗi khi tải thông tin: ${e.message}",
//                    )
//                }
//            }
//        }
    }

    fun onNameChange(textFieldValue: TextFieldValue) {
//        _uiState.update { it.copy(userName = textFieldValue) }
//        // Clear error message when user starts typing
//        if (_uiState.value.errorMessage != null) {
//            clearError()
//        }
    }

    fun uploadAvatar(imageUri: String) {
//        viewModelScope.launch {
//            _uiState.update { it.copy(isUploadingAvatar = true) }
//            try {
//                val avatarUrl = uploadAvatarUseCase(imageUri, currentUserId)
//                _uiState.update {
//                    it.copy(
//                        isUploadingAvatar = false,
//                        userAvatarUrl = avatarUrl,
//                        successMessage = "Cập nhật ảnh đại diện thành công",
//                    )
//                }
//            } catch (e: Exception) {
//                _uiState.update {
//                    it.copy(
//                        isUploadingAvatar = false,
//                        errorMessage = "Lỗi khi tải ảnh lên: ${e.message}",
//                    )
//                }
//            }
//        }
    }

    fun saveProfile() {
//        val name = _uiState.value.userName.text.trim()
//
//        if (name.isBlank()) {
//            _uiState.update { it.copy(errorMessage = "Tên không được để trống") }
//            return
//        }
//
//        if (name.length < 2) {
//            _uiState.update { it.copy(errorMessage = "Tên phải có ít nhất 2 ký tự") }
//            return
//        }
//
//        viewModelScope.launch {
//            _uiState.update { it.copy(isSaving = true) }
//            try {
//                val updatedUser = _uiState.value.currentUser?.copy(
//                    username = name,
//                    avatar = _uiState.value.userAvatarUrl,
//                )
//
//                if (updatedUser != null) {
//                    updateUserProfileUseCase(updatedUser)
//                    _uiState.update {
//                        it.copy(
//                            isSaving = false,
//                            currentUser = updatedUser,
//                            successMessage = "Cập nhật thông tin thành công",
//                        )
//                    }
//                } else {
//                    _uiState.update {
//                        it.copy(
//                            isSaving = false,
//                            errorMessage = "Không thể cập nhật thông tin",
//                        )
//                    }
//                }
//            } catch (e: Exception) {
//                _uiState.update {
//                    it.copy(
//                        isSaving = false,
//                        errorMessage = "Lỗi khi lưu thông tin: ${e.message}",
//                    )
//                }
//            }
//        }
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }

    fun clearSuccess() {
        _uiState.update { it.copy(successMessage = null) }
    }

    fun isDataChanged(): Boolean {
        val currentUser = _uiState.value.currentUser
        val currentName = _uiState.value.userName.text.trim()
        val currentAvatar = _uiState.value.userAvatarUrl

        return currentUser?.let {
            currentName != (it.username ?: "") ||
                currentAvatar != (it.avatar ?: "")
        } ?: false
    }
}
