package com.example.hotel.presentation.ui.unauthenticated.login

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.hotel.presentation.ui.common.state.ErrorState
import com.example.hotel.presentation.ui.unauthenticated.login.state.LoginErrorState
import com.example.hotel.presentation.ui.unauthenticated.login.state.LoginState
import com.example.hotel.presentation.ui.unauthenticated.login.state.LoginUiEvent
import com.example.hotel.presentation.ui.unauthenticated.login.state.loginEmptyErrorState
import com.example.hotel.presentation.ui.unauthenticated.login.state.passwordEmptyErrorState
import com.example.hotel.presentation.ui.unauthenticated.login.state.serverErrorState
import com.example.hotel.presentation.ui.unauthenticated.login.state.userNotFoundErrorState

class LoginViewModel : ViewModel() {
    var loginState = mutableStateOf(LoginState())
        private set

    fun onUiEvent(loginUiEvent: LoginUiEvent) {
        when (loginUiEvent) {
            is LoginUiEvent.LoginChanged -> {
                loginState.value = loginState.value.copy(
                    login = loginUiEvent.inputValue,
                    errorState = loginState.value.errorState.copy(
                        loginErrorState = if (loginUiEvent.inputValue.trim().isNotEmpty())
                            ErrorState()
                        else
                            loginEmptyErrorState
                    )
                )
            }

            is LoginUiEvent.PasswordChanged -> {
                loginState.value = loginState.value.copy(
                    password = loginUiEvent.inputValue,
                    errorState = loginState.value.errorState.copy(
                        passwordErrorState = if (loginUiEvent.inputValue.trim().isNotEmpty())
                            ErrorState()
                        else
                            loginEmptyErrorState
                    )
                )
            }

            is LoginUiEvent.Submit -> {
                val inputsValidated = validateInputs()
                if (inputsValidated) {
                    loginState.value =
                        loginState.value.copy(isLoading = true)
                }
            }

            is LoginUiEvent.OnUserFound -> {
                loginState.value =
                    loginState.value.copy(isLoginSuccessful = true, isLoading = false)
            }

            is LoginUiEvent.OnUserNotFound -> {
                loginState.value = loginState.value.copy(
                    isLoading = false,
                    errorState = loginState.value.errorState.copy(connectionErrorState = userNotFoundErrorState)
                )
            }
            
            is LoginUiEvent.OnServerError -> {
                loginState.value = loginState.value.copy(
                    isLoading = false,
                    errorState = loginState.value.errorState.copy(connectionErrorState = serverErrorState)
                )
            }
        }
    }

    private fun validateInputs(): Boolean {
        val loginString = loginState.value.login.trim()
        val passwordString = loginState.value.password
        return when {
            loginString.isEmpty() -> {
                loginState.value = loginState.value.copy(
                    errorState = LoginErrorState(loginErrorState = loginEmptyErrorState)
                )
                false
            }

            passwordString.isEmpty() -> {
                loginState.value = loginState.value.copy(
                    errorState = LoginErrorState(passwordErrorState = passwordEmptyErrorState)
                )
                false
            }

            else -> {
                loginState.value = loginState.value.copy(errorState = LoginErrorState())
                true
            }
        }
    }
}