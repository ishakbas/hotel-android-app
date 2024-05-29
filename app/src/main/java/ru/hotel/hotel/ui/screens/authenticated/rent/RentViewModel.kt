package ru.hotel.hotel.ui.screens.authenticated.rent

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
    var rentState = MutableStateFlow(RentState())
        private set
    var selectedRoomState = MutableStateFlow(HotelRoomsWithTypesExtended())
        private set

    init {
        onUiEvent(RentUiEvent.OnLoading)
    }

    fun onUiEvent(rentUiEvent: RentUiEvent) {
        when (rentUiEvent) {
            RentUiEvent.OnLoading -> loadInitialData()

            is RentUiEvent.OnSubmit -> {
                try {
                    viewModelScope.launch {
                        val userId =
                            withContext(Dispatchers.IO) { SharedPreferencesHelper.getUserId() }
                        val roomId =
                            withContext(Dispatchers.IO) { SharedPreferencesHelper.getRoomId() }
                        rentState.value = rentState.value.copy(
                            rent = Rent(
                                userId = userId,
                                roomId = roomId,
                                checkInDate = rentUiEvent.startDate,
                                checkOutDate = rentUiEvent.endDate,
                                status = null
                            ),
                        )

                        val response =
                            withContext(Dispatchers.IO) { repository.rentRoom(rentState.value.rent) }
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
            selectedRoomState.value = withContext(Dispatchers.IO) { repository.loadRoom(id) }
        }
    }

    private fun loadInitialData() = viewModelScope.launch {
        val checkInDate = Instant.fromEpochMilliseconds(System.currentTimeMillis())
            .toLocalDateTime(TimeZone.UTC).date

        val checkOutDate = Instant.fromEpochMilliseconds(System.currentTimeMillis())
            .toLocalDateTime(TimeZone.UTC).date

        val userId = withContext(Dispatchers.IO) { SharedPreferencesHelper.getUserId() }
        val roomId = withContext(Dispatchers.IO) { SharedPreferencesHelper.getRoomId() }

        rentState.value = rentState.value.copy(
            rent = Rent(
                userId = userId,
                roomId = roomId,
                checkInDate = checkInDate,
                checkOutDate = checkOutDate,
                status = null
            ),
            isRentSuccess = false,
            hasError = false
        )
    }
}