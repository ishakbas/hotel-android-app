package ru.hotel.hotel.ui.screens.authenticated.profile.state

import ru.ktor_koin.network.model.HotelRoom

data class HotelUploadState(
    val hotelRoom: HotelRoom = HotelRoom(),
    val loading: Boolean = false,
    val success: Boolean = false,
    val error: Boolean = false,
)