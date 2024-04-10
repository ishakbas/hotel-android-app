package ru.hotel.hotel.ui.screens.unauthenticated.login.state

import com.example.hotel.presentation.ui.common.state.ErrorState

val loginEmptyErrorState =
    ErrorState(
        hasError = true,
        errorMessageStringResource = "Пожалуйста введите логин"
    )

val passwordEmptyErrorState =
    ErrorState(
        hasError = true,
        errorMessageStringResource = "Пожалуйста введите пароль"
    )

val userNotFoundErrorState =
    ErrorState(hasError = true, errorMessageStringResource = "Неверный логин или пароль")

val serverErrorState =
    ErrorState(hasError = true, errorMessageStringResource = "Ошибка сервера")

val userNotCreated =
    ErrorState(hasError = true, errorMessageStringResource = "Такой пользователь уже существует")
