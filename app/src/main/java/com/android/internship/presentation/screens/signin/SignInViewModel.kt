package com.android.internship.presentation.screens.signin

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.android.internship.di.AppContainer
import com.android.internship.domain.usecase.SignInUseCase
import com.android.internship.presentation.components.Validator
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SignInViewModel(
    private val validator: Validator,
    private val signInUseCase: SignInUseCase,
) : ViewModel() {
    private val _state = MutableStateFlow(SignInState())
    val state get() = _state.asStateFlow()

    fun updateEmailState(email: String) {
        val trimmedEmail = email.trim()
        val errorMessage = validator.emailValidator(email = trimmedEmail)
        _state.update {
            it.copy(
                emailState = it.emailState.copy(
                    value = trimmedEmail,
                    isError = errorMessage != null,
                    errorMessage = errorMessage,
                ),
            )
        }
    }

    fun updatePasswordState(password: String) {
        val trimmedPassword = password.trim()
        val errorMessage = validator.passwordValidator(password = trimmedPassword)
        _state.update {
            it.copy(
                passwordState = it.passwordState.copy(
                    value = trimmedPassword,
                    isError = errorMessage != null,
                    errorMessage = errorMessage,
                ),
            )
        }
    }

    fun signIn() {
        viewModelScope.launch {
            _state.update {
                it.copy(isLoading = true)
            }

            val response = signInUseCase(
                email = state.value.emailState.value,
                password = state.value.passwordState.value,
            )

            delay(1000)

            _state.update {
                it.copy(
                    isLoading = false,
                    signInSuccess = response.success,
                    message = response.message,
                )
            }
        }
    }

    fun clearMessage() {
        _state.update {
            it.copy(message = null)
        }
    }

    companion object {
        fun factory(context: Context) = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(SignInViewModel::class.java)) {
                    val validator = Validator(context = context)
                    val signInUseCase = SignInUseCase(repository = AppContainer(context).authRepository)

                    return SignInViewModel(
                        validator = validator,
                        signInUseCase = signInUseCase,
                    ) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }
    }
}
