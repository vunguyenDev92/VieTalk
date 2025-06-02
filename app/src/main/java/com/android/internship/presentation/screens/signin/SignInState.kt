package com.android.internship.presentation.screens.signin

import com.android.internship.presentation.components.TextFieldState

data class SignInState(
    val emailState: TextFieldState = TextFieldState(),
    val passwordState: TextFieldState = TextFieldState(),
    val isLoading: Boolean = false,
    val signInSuccess: Boolean = false,
    val errorMessage: String? = null,
)
