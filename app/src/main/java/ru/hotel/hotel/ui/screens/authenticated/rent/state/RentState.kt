package ru.hotel.hotel.ui.screens.authenticated.rent.state

import ru.ktor_koin.network.model.Rent

data class RentState(
    var rent: Rent = Rent(),
    val isRentSuccess: Boolean = false,
    val hasError: Boolean = false
)
