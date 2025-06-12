package com.android.internship.presentation.screens.signup

import com.android.internship.presentation.components.TextFieldState

data class SignUpState(
    val emailState: TextFieldState = TextFieldState(),
    val passwordState: TextFieldState = TextFieldState(),
    val confirmPasswordState: TextFieldState = TextFieldState(),
    val isLoading: Boolean = false,
    val signUpSuccess: Boolean = false,
    val message: String? = null,
)
