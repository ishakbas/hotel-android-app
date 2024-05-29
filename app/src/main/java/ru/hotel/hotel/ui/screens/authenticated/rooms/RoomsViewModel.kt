package ru.hotel.hotel.ui.screens.authenticated.rooms

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.hotel.hotel.ui.screens.authenticated.rooms.state.RoomsErrorState
import ru.hotel.hotel.ui.screens.authenticated.rooms.state.RoomsState
import ru.hotel.hotel.ui.screens.authenticated.rooms.state.RoomsUiEvent
import ru.hotel.hotel.ui.screens.authenticated.rooms.state.serverErrorState
import ru.ktor_koin.repositories.RoomsRepository

class RoomsViewModel(private val roomsRepository: RoomsRepository) : ViewModel() {
    var roomsState = MutableStateFlow(RoomsState(isLoading = true))
        private set

    init {
        loadRooms()
    }

    fun onUiEvent(event: RoomsUiEvent) {
        when (event) {
            is RoomsUiEvent.OnLoading -> {
                roomsState.value = roomsState.value.copy(isLoading = true)
            }

            is RoomsUiEvent.OnLoaded -> roomsState.value =
                roomsState.value.copy(isLoading = false)

            is RoomsUiEvent.OnServerError -> roomsState.value =
                roomsState.value.copy(errorState = RoomsErrorState(connectionErrorState = serverErrorState))
        }
    }

    private fun loadRooms() = viewModelScope.launch {
        try {
            val rooms = withContext(Dispatchers.IO) { roomsRepository.loadRooms() }
            roomsState.value =
                roomsState.value.copy(roomList = rooms, isLoading = false)
            onUiEvent(RoomsUiEvent.OnLoaded)
        } catch (ex: Exception) {
            onUiEvent(RoomsUiEvent.OnServerError)
        }
    }
}