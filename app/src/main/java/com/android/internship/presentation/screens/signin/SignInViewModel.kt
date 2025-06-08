package com.android.internship.presentation.screens.signin

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.android.internship.di.AppContainer
import com.android.internship.domain.usecase.SignInUseCase
import com.android.internship.presentation.components.Validator
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
        val errorMessage = validator.emailValidator(email = email)
        _state.update {
            it.copy(
                emailState = it.emailState.copy(
                    value = email.trim(),
                    isError = errorMessage != null,
                    errorMessage = errorMessage,
                ),
            )
        }
    }

    fun updatePasswordState(password: String) {
        val errorMessage = validator.passwordValidator(password = password)
        _state.update {
            it.copy(
                passwordState = it.passwordState.copy(
                    value = password.trim(),
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

            _state.update {
                it.copy(
                    isLoading = false,
                    signInSuccess = response.success,
                    errorMessage = response.message,
                )
            }
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
