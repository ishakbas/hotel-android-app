package ru.hotel.hotel.ui.screens.unauthenticated.registration

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hotel.presentation.ui.common.state.ErrorState
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.launch
import ru.hotel.hotel.ui.screens.unauthenticated.login.state.loginEmptyErrorState
import ru.hotel.hotel.ui.screens.unauthenticated.login.state.passwordEmptyErrorState
import ru.hotel.hotel.ui.screens.unauthenticated.login.state.serverErrorState
import ru.hotel.hotel.ui.screens.unauthenticated.login.state.userNotCreated
import ru.hotel.hotel.ui.screens.unauthenticated.registration.state.RegistrationErrorState
import ru.hotel.hotel.ui.screens.unauthenticated.registration.state.RegistrationState
import ru.hotel.hotel.ui.screens.unauthenticated.registration.state.RegistrationUiEvent
import ru.ktor_koin.network.model.User
import ru.ktor_koin.repositories.LoginRepository

class RegistrationViewModel(private val loginRepository: LoginRepository) : ViewModel() {
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
                    viewModelScope.launch {
                        with(registrationState.value) {
                            val result =
                                loginRepository.registerUser(
                                    User(
                                        login = login,
                                        password = password,
                                        admin = false
                                    )
                                )
                            when (result) {
                                HttpStatusCode.Created -> onUiEvent(RegistrationUiEvent.OnUserCreate)
                                HttpStatusCode.Conflict -> onUiEvent(RegistrationUiEvent.OnUserNotCreate)
                                HttpStatusCode.InternalServerError -> onUiEvent(RegistrationUiEvent.OnServerError)
                            }
                        }
                    }
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