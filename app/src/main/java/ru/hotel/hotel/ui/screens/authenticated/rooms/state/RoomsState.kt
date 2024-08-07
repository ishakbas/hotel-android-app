package ru.hotel.hotel.ui.screens.authenticated.rooms.state

import com.example.hotel.presentation.ui.common.state.ErrorState
import ru.ktor_koin.network.model.HotelRoom


data class RoomsState(
    val roomList: List<HotelRoom> = emptyList(),
    val errorState: RoomsErrorState = RoomsErrorState(),
    val isLoading: Boolean = false
)

data class RoomsErrorState(
    val connectionErrorState: ErrorState = ErrorState(),
    val emptyRoomsError: ErrorState = ErrorState()
)