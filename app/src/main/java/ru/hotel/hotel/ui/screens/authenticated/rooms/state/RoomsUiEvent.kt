package ru.hotel.hotel.ui.screens.authenticated.rooms.state

sealed class RoomsUiEvent {
    data object OnLoading : RoomsUiEvent()
    data object OnLoaded : RoomsUiEvent()
    data object OnServerError : RoomsUiEvent()
}