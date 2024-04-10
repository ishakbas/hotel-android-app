package ru.hotel.hotel.ui.screens.authenticated.rent.state

import kotlinx.datetime.LocalDate

sealed class RentUiEvent {
    data object OnLoading : RentUiEvent()
    data class OnSubmit(val startDate: LocalDate, val endDate: LocalDate) : RentUiEvent()
    data object OnRentSuccessful : RentUiEvent()
    data object OnServerError : RentUiEvent()
}