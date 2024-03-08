package com.example.hotel.presentation.ui.authenticated.room

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material3.Card
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.DateRangePickerState
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.hotel.data.remote.UserInfo
import com.example.hotel.data.remote.api.KtorApiClient
import com.example.hotel.presentation.ui.common.customComposables.CustomOutlinedButton
import com.example.hotel.data.remote.HotelRoom
import com.example.navbarexample.data.remote.Rent
import com.example.hotel.presentation.ui.authenticated.rooms.Roomd
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoomDetail(roomId: Int, ktorApiClient: KtorApiClient, userInfo: UserInfo) {
    val dateRangePickerState =
        rememberDateRangePickerState(yearRange = 2023..2025, initialDisplayMode = DisplayMode.Input)

    val scope = rememberCoroutineScope()

    var hotelRoom by remember {
        mutableStateOf(HotelRoom(0, "", "", ByteArray(0)))
    }

    LaunchedEffect(key1 = true) {
        hotelRoom = ktorApiClient.getRoom(roomId)
    }

    Column(
        modifier = Modifier.padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Roomd(room = hotelRoom, {})
        HorizontalDivider(Modifier.padding(10.dp))
        Sa(dateRangePickerState = dateRangePickerState)
        HorizontalDivider(Modifier.padding(10.dp))
        CustomOutlinedButton(label = "Арендовать") {
            scope.launch {
                ktorApiClient.rentRoom(
                    Rent(
                        userInfo.id,
                        hotelRoom.id,
                        LocalDate(2023, 12, 12),
                        LocalDate(2023, 12, 15),
                        status = null
                    )
                )
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Sa(dateRangePickerState: DateRangePickerState) {
    Card(
        Modifier
            .fillMaxWidth()
    ) {
        DateRangePicker(
            state = dateRangePickerState,
            headline = {
                Row {
                    Icon(imageVector = Icons.Outlined.DateRange, contentDescription = null)
                    Text(text = "Даты бронирования")
                }
            }
        )
    }
}

