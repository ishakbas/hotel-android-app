package ru.hotel.hotel.ui.screens.authenticated.rent

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.launch
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import ru.hotel.hotel.ui.screens.authenticated.rent.state.RentState
import ru.hotel.hotel.ui.screens.authenticated.rent.state.RentUiEvent
import ru.ktor_koin.network.model.HotelRoomsWithTypesExtended
import ru.ktor_koin.network.model.Rent
import ru.ktor_koin.network.sharedPrefs.SharedPreferencesHelper
import ru.ktor_koin.repositories.RoomsRepository

class RentViewModel(private val repository: RoomsRepository) : ViewModel() {
    var rentState = mutableStateOf(RentState())
        private set
    var selectedRoomState = mutableStateOf(HotelRoomsWithTypesExtended())
        private set

    fun onUiEvent(rentUiEvent: RentUiEvent) {
        when (rentUiEvent) {
            RentUiEvent.OnLoading -> viewModelScope.launch {
                val checkInDate = Instant.fromEpochMilliseconds(System.currentTimeMillis())
                    .toLocalDateTime(TimeZone.UTC).date

                val checkOutDate = Instant.fromEpochMilliseconds(System.currentTimeMillis())
                    .toLocalDateTime(TimeZone.UTC).date

                rentState.value = rentState.value.copy(
                    rent = Rent(
                        userId = SharedPreferencesHelper.getUserId(),
                        roomId = SharedPreferencesHelper.getRoomId(),
                        checkInDate = checkInDate,
                        checkOutDate = checkOutDate,
                        status = null
                    ),
                    isRentSuccess = false,
                    hasError = false
                )
            }

            is RentUiEvent.OnSubmit -> {
                try {
                    val userId = SharedPreferencesHelper.getUserId()
                    val roomId = SharedPreferencesHelper.getRoomId()
                    rentState.value = rentState.value.copy(
                        rent = Rent(
                            userId = userId,
                            roomId = roomId,
                            checkInDate = rentUiEvent.startDate,
                            checkOutDate = rentUiEvent.endDate,
                            status = null
                        ),
                    )
                    viewModelScope.launch {
                        val response = repository.rentRoom(rentState.value.rent)
                        when (response) {
                            HttpStatusCode.Created -> onUiEvent(RentUiEvent.OnRentSuccessful)
                            HttpStatusCode.InternalServerError -> onUiEvent(RentUiEvent.OnServerError)
                        }
                    }
                } catch (e: Exception) {
                    onUiEvent(RentUiEvent.OnServerError)
                }
            }

            RentUiEvent.OnServerError -> rentState.value =
                rentState.value.copy(hasError = true, isRentSuccess = false)

            RentUiEvent.OnRentSuccessful -> rentState.value =
                rentState.value.copy(hasError = false, isRentSuccess = true)
        }

    }

    fun getRoom(id: Int) {
        viewModelScope.launch {
            selectedRoomState.value = repository.loadRoom(id)
        }
    }

    init {
        onUiEvent(RentUiEvent.OnLoading)
    }
}