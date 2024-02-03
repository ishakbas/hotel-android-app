package com.example.hotel.presentation.ui.authenticated.rooms.state

import com.example.hotel.presentation.ui.common.state.ErrorState


val emptyErrorState = ErrorState(hasError = true, errorMessageStringResource = "Комнаты не найдены")

val serverErrorState =
    ErrorState(hasError = true, errorMessageStringResource = "Ошибка сервера")