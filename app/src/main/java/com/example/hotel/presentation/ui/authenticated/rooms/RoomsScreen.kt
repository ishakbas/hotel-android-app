package com.example.hotel.presentation.ui.authenticated.rooms

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.hotel.data.remote.HotelRoom
import com.example.hotel.data.remote.UserInfo

@Composable
fun RoomsScreen(
    roomsViewModel: RoomsViewModel = viewModel(),
    userInfo: UserInfo,
    onCardClick: (Int) -> Unit,
) {
    val roomsState by remember {
        roomsViewModel.roomsState
    }

    if (roomsState.isLoading) {
        CircularProgressIndicator()
    } else {
        RoomList(list = roomsState.roomList, onCardClick)
    }
}


@Composable
fun RoomsScreen1() {

}


@Composable
fun RoomList(list: List<HotelRoom>, onCardClick: (Int) -> Unit) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(6.dp),
        contentPadding = PaddingValues(6.dp),
        modifier = Modifier.padding(8.dp)
    ) {
        items(list) { room ->
            Roomd(room = room, onCardClick)
        }
    }
}

@Composable
fun Roomd(room: HotelRoom, onCardClick: (Int) -> Unit, modifier: Modifier = Modifier) {
    Card(modifier = Modifier
        .fillMaxWidth()
        .heightIn(min = 200.dp, max = 300.dp)
        .clickable {
            onCardClick(room.id)
        }) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 185.dp)
        ) {
            AsyncImage(
                model = room.room_image ,
                contentDescription = null,
                contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
            )
        }
        Column(
            Modifier
                .padding(10.dp)
                .fillMaxWidth()
        ) {
            Text(text = room.name, style = MaterialTheme.typography.displaySmall)
            Text(
                text = "Тип комнаты: ${room.roomType}",
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}
