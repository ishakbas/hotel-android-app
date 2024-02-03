package com.example.hotel.presentation.ui.authenticated.rooms

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.hotel.data.TAG
import com.example.hotel.data.repository.RoomsRepository
import com.example.hotel.presentation.ui.authenticated.rooms.state.RoomsState
import com.example.hotel.presentation.ui.authenticated.rooms.state.RoomsUiEvent
import com.example.hotel.presentation.ui.authenticated.rooms.state.serverErrorState
import kotlinx.coroutines.launch

@Suppress("UNCHECKED_CAST")
class RoomsViewModel(private val repository: RoomsRepository) : ViewModel() {
    var roomsState = mutableStateOf(RoomsState())
        private set

    init {
        fetchRooms()
    }

    fun onUiEvent(roomsUiEvent: RoomsUiEvent) {
        when (roomsUiEvent) {
            is RoomsUiEvent.OnLoading -> {
                fetchRooms()
                roomsState.value = roomsState.value.copy(
                    isLoading = true
                )
                onUiEvent(RoomsUiEvent.OnLoaded)
            }

            is RoomsUiEvent.OnLoaded -> {
                roomsState.value = roomsState.value.copy(
                    isLoading = false
                )
            }

            is RoomsUiEvent.OnServerError -> {
                roomsState.value = roomsState.value.copy(
                    isLoading = false,
                    errorState = roomsState.value.errorState.copy(connectionErrorState = serverErrorState)
                )
            }
        }
    }

    private fun fetchRooms() {
        viewModelScope.launch {
            roomsState.value = roomsState.value.copy(roomList = repository.fetchRooms())
            Log.d(TAG, "fetchedRooms: ${repository.fetchRooms()}")
            Log.d(TAG, "fetchRooms in state: ${roomsState.value.roomList}")
        }
    }


    class HotelRoomViewModelFactory(private val repository: RoomsRepository) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(RoomsViewModel::class.java)) {
                return RoomsViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}