package ru.hotel.hotel.ui.screens.authenticated.rent

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Kitchen
import androidx.compose.material.icons.filled.SingleBed
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.load.engine.DiskCacheStrategy
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import ru.hotel.hotel.ui.screens.authenticated.rent.state.RentUiEvent
import ru.ktor_koin.network.model.HotelRoomsWithTypesExtended


@Composable
fun RentScreen(
    id: Int,
    modifier: Modifier = Modifier,
    rentViewModel: RentViewModel
) {
    val roomInfoState by remember {
        rentViewModel.selectedRoomState
    }
    rentViewModel.getRoom(id)
    RoomContainer(
        modifier = modifier,
        room = roomInfoState,
        rentViewModel = rentViewModel
    )
}

@Composable
fun RoomContainer(
    modifier: Modifier = Modifier,
    rentViewModel: RentViewModel,
    room: HotelRoomsWithTypesExtended
) {
    val context = LocalContext.current

    val rentSuccess by remember {
        mutableStateOf(rentViewModel.rentState.value.isRentSuccess)
    }
    val rentError by remember {
        mutableStateOf(rentViewModel.rentState.value.hasError)
    }

    if (rentError) {
        Toast.makeText(context, "Ошибка. Повторите попытку", Toast.LENGTH_SHORT).show()
    }

    if (rentSuccess) {
        Toast.makeText(context, "Комната успешно арендована", Toast.LENGTH_SHORT).show()
    }

    Column(modifier.fillMaxSize()) {
        RoomCard(room = room, onCardClick = {})
        HorizontalDivider(modifier = Modifier.padding(4.dp), thickness = 2.dp)
        RoomInfo(roomInfo = room)
        HorizontalDivider(modifier = Modifier.padding(4.dp), thickness = 2.dp)
        RentInfo(rentViewModel = rentViewModel)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RentInfo(rentViewModel: RentViewModel, modifier: Modifier = Modifier) {
    val dialogState = remember {
        mutableStateOf(false)
    }
    val dateState =
        rememberDateRangePickerState(
            initialSelectedStartDateMillis = System.currentTimeMillis(),
            initialSelectedEndDateMillis = System.currentTimeMillis(),
            yearRange = 2024..2024
        )
    val startDate = dateState.selectedStartDateMillis?.let { Instant.fromEpochMilliseconds(it) }
        ?.toLocalDateTime(
            TimeZone.UTC
        )?.date!!
    val endDate = dateState.selectedEndDateMillis?.let { Instant.fromEpochMilliseconds(it) }
        ?.toLocalDateTime(
            TimeZone.UTC
        )?.date


    Card(modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(4.dp)) {
            Button(modifier = Modifier.fillMaxWidth(), onClick = { dialogState.value = true }) {
                Text(text = "Выбрать даты аренды")
            }
            Text(text = "Выбранные даты аренды:")
            Text(text = "с $startDate по $endDate")
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    endDate?.let { RentUiEvent.OnSubmit(startDate, it) }
                        ?.let { rentViewModel.onUiEvent(it) }
                }) {
                Text(text = "Арендовать")
            }
        }
    }

    if (dialogState.value) {
        Dialog(onDismissRequest = { dialogState.value = false }) {
            DateRangePicker(state = dateState)
        }
    }
}

@Composable
fun RoomInfo(
    modifier: Modifier = Modifier,
    roomInfo: HotelRoomsWithTypesExtended
) {
    Card(modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .padding(6.dp)
                .fillMaxWidth()
        ) {
            Text(text = "Описание: ", style = MaterialTheme.typography.bodyLarge)
            Text(text = roomInfo.description.toString())
            Spacer(modifier = Modifier.height(10.dp))
            RowIconInfo(
                text = "Количество комнат: ${roomInfo.bedCount}",
                icon = Icons.Filled.SingleBed
            )
            if (roomInfo.wifi != null) {
                RowIconInfo(text = "Wi-Fi", icon = Icons.Filled.Wifi)
            }
            if (roomInfo.kitchen != null) {
                RowIconInfo(
                    text = "Кухня",
                    icon = Icons.Filled.Kitchen
                )
            }
            if (roomInfo.tv != null) {
                RowIconInfo(
                    text = "Телевизор",
                    icon = Icons.Filled.Tv
                )
            }
        }
    }
}

@Composable
fun RowIconInfo(modifier: Modifier = Modifier, text: String, icon: ImageVector) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = modifier) {
        Icon(imageVector = icon, contentDescription = null, modifier = Modifier.padding(4.dp))
        Text(text = text, style = MaterialTheme.typography.bodyLarge)
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun RoomCard(
    room: HotelRoomsWithTypesExtended,
    onCardClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier
        .fillMaxWidth()
        .heightIn(min = 150.dp, max = 300.dp)
        .clickable {
            onCardClick(room.id)
        }) {
        Column(
            Modifier
                .padding(10.dp)
                .fillMaxWidth()
        ) {
            GlideImage(
                model = room.roomImage,
                modifier = Modifier
                    .fillMaxWidth()
                    .size(200.dp)
                    .clip(RoundedCornerShape(6.dp)),
                contentScale = ContentScale.FillBounds,
                contentDescription = null
            ) {
                it.override(400).diskCacheStrategy(DiskCacheStrategy.RESOURCE)
            }
            Text(text = room.name, style = MaterialTheme.typography.headlineMedium)
            Text(
                text = "Тип комнаты: ${room.roomTypeName}",
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}