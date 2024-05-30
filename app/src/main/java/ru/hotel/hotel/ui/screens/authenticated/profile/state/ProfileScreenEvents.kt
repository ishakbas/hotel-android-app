package ru.hotel.hotel.ui.screens.authenticated.profile.state

sealed class ProfileScreenEvents {
    data object START : ProfileScreenEvents()
    data object LOADING : ProfileScreenEvents()
    data object SUCCESS : ProfileScreenEvents()
    data object ERROR : ProfileScreenEvents()
}