package ru.hotel.hotel.ui.screens.unauthenticated.login.state

sealed class LoginUiEvent {
    data class LoginChanged(val inputValue: String) : LoginUiEvent()
    data class PasswordChanged(val inputValue: String) : LoginUiEvent()
    data object Submit : LoginUiEvent()
    data object OnUserFound : LoginUiEvent()
    data object OnUserNotFound : LoginUiEvent()
    data object OnServerError : LoginUiEvent()
}