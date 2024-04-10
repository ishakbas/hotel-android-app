package ru.hotel.hotel.ui.screens.authenticated.rooms

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.hotel.hotel.ui.screens.authenticated.rooms.state.RoomsErrorState
import ru.hotel.hotel.ui.screens.authenticated.rooms.state.RoomsState
import ru.hotel.hotel.ui.screens.authenticated.rooms.state.RoomsUiEvent
import ru.hotel.hotel.ui.screens.authenticated.rooms.state.serverErrorState
import ru.ktor_koin.repositories.RoomsRepository

class RoomsViewModel(private val roomsRepository: RoomsRepository) : ViewModel() {
    var roomsState = mutableStateOf(RoomsState(isLoading = true))
        private set


    fun onUiEvent(event: RoomsUiEvent) {
        when (event) {
            is RoomsUiEvent.OnLoaded -> roomsState.value =
                roomsState.value.copy(isLoading = false)

            is RoomsUiEvent.OnLoading -> {
                roomsState.value = roomsState.value.copy(isLoading = true)
                viewModelScope.launch {
                    try {
                        val rooms = roomsRepository.loadRooms()
                        roomsState.value =
                            roomsState.value.copy(roomList = rooms, isLoading = false)
                        onUiEvent(RoomsUiEvent.OnLoaded)
                    } catch (ex: Exception) {
                        onUiEvent(RoomsUiEvent.OnServerError)
                    }
                }
            }

            is RoomsUiEvent.OnServerError -> roomsState.value =
                roomsState.value.copy(errorState = RoomsErrorState(connectionErrorState = serverErrorState))
        }
    }


}