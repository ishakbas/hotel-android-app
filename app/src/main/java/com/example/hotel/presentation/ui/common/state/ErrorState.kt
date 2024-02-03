package com.example.hotel.presentation.ui.common.state

data class ErrorState(
    val hasError: Boolean = false,
    val errorMessageStringResource: String = ""
)