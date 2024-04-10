package ru.hotel.hotel.ui.screens.unauthenticated.registration.state

import com.example.hotel.presentation.ui.common.state.ErrorState

data class RegistrationState(
    val login: String = "",
    val password: String = "",
    val errorState: RegistrationErrorState = RegistrationErrorState(),
    val isLoading: Boolean = false,
    val isRegisterSuccessful: Boolean = false
)

data class RegistrationErrorState(
    val connectionErrorState: ErrorState = ErrorState(),
    val loginErrorState: ErrorState = ErrorState(),
    val passwordErrorState: ErrorState = ErrorState()
)