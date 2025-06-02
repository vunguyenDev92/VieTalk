package com.android.internship.presentation.components

data class TextFieldState(
    val value: String = "",
    val isError: Boolean = true,
    val errorMessage: String? = null,
)
