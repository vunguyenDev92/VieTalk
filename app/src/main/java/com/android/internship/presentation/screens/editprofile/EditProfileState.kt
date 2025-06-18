package com.android.internship.presentation.screens.editprofile

import androidx.compose.ui.text.input.TextFieldValue
import com.android.internship.data.model.User

data class EditProfileState(
    val isLoading: Boolean = true,
    val isSaving: Boolean = false,
    val isUploadingAvatar: Boolean = false,
    val errorMessage: String? = null,
    val showDialog: Boolean = false,
    val successMessage: String? = null,
    val currentUser: User? = null,
    val userName: TextFieldValue = TextFieldValue(""),
    val userEmail: String = "",
    val userAvatarUrl: String? = null,
)
