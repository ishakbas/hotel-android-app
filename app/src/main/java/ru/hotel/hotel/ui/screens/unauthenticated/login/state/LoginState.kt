package ru.hotel.hotel.ui.screens.unauthenticated.login.state

import com.example.hotel.presentation.ui.common.state.ErrorState


data class LoginState(
    val login: String = "",
    val password: String = "",
    val errorState: LoginErrorState = LoginErrorState(),
    val isLoading: Boolean = false,
    val isLoginSuccessful: Boolean = false
)

data class LoginErrorState(
    val connectionErrorState: ErrorState = ErrorState(),
    val loginErrorState: ErrorState = ErrorState(),
    val passwordErrorState: ErrorState = ErrorState()
)