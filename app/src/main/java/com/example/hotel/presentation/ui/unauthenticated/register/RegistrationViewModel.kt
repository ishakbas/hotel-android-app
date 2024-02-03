package com.example.hotel.presentation.ui.unauthenticated.register

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.hotel.presentation.ui.common.state.ErrorState
import com.example.hotel.presentation.ui.unauthenticated.login.state.loginEmptyErrorState
import com.example.hotel.presentation.ui.unauthenticated.login.state.passwordEmptyErrorState
import com.example.hotel.presentation.ui.unauthenticated.login.state.serverErrorState
import com.example.hotel.presentation.ui.unauthenticated.login.state.userNotCreated
import com.example.hotel.presentation.ui.unauthenticated.register.state.RegistrationErrorState
import com.example.hotel.presentation.ui.unauthenticated.register.state.RegistrationState
import com.example.hotel.presentation.ui.unauthenticated.register.state.RegistrationUiEvent

class RegistrationViewModel : ViewModel() {
    var registrationState = mutableStateOf(RegistrationState())
        private set

    fun onUiEvent(registrationUiEvent: RegistrationUiEvent) {
        when (registrationUiEvent) {
            is RegistrationUiEvent.LoginChanged -> {
                registrationState.value = registrationState.value.copy(
                    login = registrationUiEvent.inputValue,
                    errorState = registrationState.value.errorState.copy(
                        loginErrorState = if (registrationUiEvent.inputValue.trim().isNotEmpty())
                            ErrorState()
                        else
                            loginEmptyErrorState
                    )
                )
            }

            is RegistrationUiEvent.PasswordChanged -> {
                registrationState.value = registrationState.value.copy(
                    password = registrationUiEvent.inputValue,
                    errorState = registrationState.value.errorState.copy(
                        passwordErrorState = if (registrationUiEvent.inputValue.trim().isNotEmpty())
                            ErrorState()
                        else
                            loginEmptyErrorState
                    )
                )
            }

            is RegistrationUiEvent.Submit -> {
                val inputsValidated = validateInputs()
                if (inputsValidated) {
                    registrationState.value = registrationState.value.copy(isLoading = true)
                }
            }

            is RegistrationUiEvent.OnUserCreate -> {
                registrationState.value =
                    registrationState.value.copy(isRegisterSuccessful = true, isLoading = false)
            }

            is RegistrationUiEvent.OnUserNotCreate -> {
                registrationState.value = registrationState.value.copy(
                    isLoading = false,
                    errorState = registrationState.value.errorState.copy(connectionErrorState = userNotCreated)
                )
            }

            is RegistrationUiEvent.OnServerError -> {
                registrationState.value = registrationState.value.copy(
                    isLoading = false,
                    errorState = registrationState.value.errorState.copy(connectionErrorState = serverErrorState)
                )
            }
        }
    }

    private fun validateInputs(): Boolean {
        val loginString = registrationState.value.login.trim()
        val passwordString = registrationState.value.password

        return when {
            loginString.isEmpty() -> {
                registrationState.value = registrationState.value.copy(
                    errorState = RegistrationErrorState(loginErrorState = loginEmptyErrorState)
                )
                false
            }

            passwordString.isEmpty() -> {
                registrationState.value = registrationState.value.copy(
                    errorState = RegistrationErrorState(passwordErrorState = passwordEmptyErrorState)
                )
                false
            }

            else -> {
                registrationState.value =
                    registrationState.value.copy(errorState = RegistrationErrorState())
                true
            }
        }
    }
}