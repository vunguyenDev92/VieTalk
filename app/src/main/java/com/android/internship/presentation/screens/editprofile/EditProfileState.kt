package com.android.internship.presentation.screens.editprofile

import androidx.compose.ui.text.input.TextFieldValue
import com.android.internship.data.model.User

data class EditProfileState(
    val isLoading: Boolean = false,
    val currentUser: User? = null,
    val userName: TextFieldValue = TextFieldValue(""),
    val userEmail: String = "",
    val userAvatarUrl: String = "",
    val isUploadingAvatar: Boolean = false,
    val isSaving: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null,
)
