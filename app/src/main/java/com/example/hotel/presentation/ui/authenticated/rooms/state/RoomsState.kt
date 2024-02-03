package com.example.hotel.presentation.ui.authenticated.rooms.state

import com.example.hotel.presentation.ui.common.state.ErrorState
import com.example.hotel.data.remote.HotelRoom


data class RoomsState(
    var roomList: MutableList<HotelRoom> = mutableListOf(),
    val errorState: RoomsErrorState = RoomsErrorState(),
    val isLoading: Boolean = false
)

data class RoomsErrorState(
    val connectionErrorState: ErrorState = ErrorState(),
    val emptyRoomsError: ErrorState = ErrorState()
)