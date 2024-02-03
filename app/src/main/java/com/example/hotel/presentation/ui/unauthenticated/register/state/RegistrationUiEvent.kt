package com.example.hotel.presentation.ui.unauthenticated.register.state

sealed class RegistrationUiEvent {
    data class LoginChanged(val inputValue: String) : RegistrationUiEvent()
    data class PasswordChanged(val inputValue: String) : RegistrationUiEvent()
    data object Submit : RegistrationUiEvent()
    data object OnUserCreate : RegistrationUiEvent()
    data object OnUserNotCreate : RegistrationUiEvent()
    data object OnServerError : RegistrationUiEvent()
}