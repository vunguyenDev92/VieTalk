package com.android.internship.presentation.screens.signup

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.android.internship.di.AppContainer
import com.android.internship.domain.usecase.SignUpUseCase
import com.android.internship.presentation.components.Validator
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SignUpViewModel(
    private val validator: Validator,
    private val signUpUseCase: SignUpUseCase,
) : ViewModel() {
    private val _state = MutableStateFlow(SignUpState())
    val state get() = _state.asStateFlow()

    fun updateEmailState(email: String) {
        val emailWithoutSpaces = email.replace(Regex("\\s"), "")
        val errorMessage = validator.emailValidator(email = emailWithoutSpaces)
        _state.update {
            it.copy(
                emailState = it.emailState.copy(
                    value = emailWithoutSpaces,
                    isError = errorMessage != null,
                    errorMessage = errorMessage,
                ),
            )
        }
    }

    fun updatePasswordState(password: String) {
        val passwordWithoutSpaces = password.replace(Regex("\\s"), "")
        val errorMessage = validator.passwordValidator(password = passwordWithoutSpaces)
        _state.update {
            it.copy(
                passwordState = it.passwordState.copy(
                    value = passwordWithoutSpaces,
                    isError = errorMessage != null,
                    errorMessage = errorMessage,
                ),
            )
        }
        updateConfirmPasswordState(confirmPassword = state.value.confirmPasswordState.value)
    }

    fun updateConfirmPasswordState(confirmPassword: String) {
        val confirmPasswordWithoutSpaces = confirmPassword.replace(Regex("\\s"), "")
        val errorMessage = validator.confirmPasswordValidator(
            password = state.value.passwordState.value,
            confirmPassword = confirmPasswordWithoutSpaces,
        )

        _state.update {
            it.copy(
                confirmPasswordState = it.confirmPasswordState.copy(
                    value = confirmPasswordWithoutSpaces,
                    isError = errorMessage != null,
                    errorMessage = errorMessage,
                ),
            )
        }
    }

    fun signUp() {
        viewModelScope.launch {
            _state.update {
                it.copy(isLoading = true)
            }

            val response = signUpUseCase(
                email = state.value.emailState.value,
                password = state.value.passwordState.value,
            )

            delay(1000)

            response.onSuccess { message ->
                _state.update {
                    it.copy(
                        isLoading = false,
                        signUpSuccess = true,
                        message = message,
                    )
                }
            }.onFailure { exception ->
                _state.update {
                    it.copy(
                        isLoading = false,
                        signUpSuccess = false,
                        message = exception.message,
                    )
                }
            }
        }
    }

    fun clearMessage() {
        _state.update {
            it.copy(message = null)
        }
    }

    companion object {
        fun factory(context: Context, appContainer: AppContainer) = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(SignUpViewModel::class.java)) {
                    val validator = Validator(context = context)

                    val signUpUseCase = SignUpUseCase(
                        authRepository = appContainer.authRepository,
                        userRepository = appContainer.userRepository,
                    )

                    return SignUpViewModel(
                        validator = validator,
                        signUpUseCase = signUpUseCase,
                    ) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }
    }
}
