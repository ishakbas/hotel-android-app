package ru.hotel.hotel.ui.screens.authenticated.rooms.state

import com.example.hotel.presentation.ui.common.state.ErrorState

val serverErrorState =
    ErrorState(hasError = true, errorMessageStringResource = "Ошибка сервера")