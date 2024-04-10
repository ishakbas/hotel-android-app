package ru.hotel.hotel.ui.screens.authenticated.rooms

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.GlideSubcomposition
import com.bumptech.glide.integration.compose.RequestState
import com.bumptech.glide.load.engine.DiskCacheStrategy
import ru.hotel.hotel.ui.screens.authenticated.rooms.state.RoomsUiEvent
import ru.ktor_koin.network.model.HotelRoom

@Composable
fun RoomsScreen(
    modifier: Modifier = Modifier,
    roomScreenViewModel: RoomsViewModel, onCardClick: (Int) -> Unit
) {
    val roomsState by remember {
        roomScreenViewModel.roomsState
    }

    Box(modifier.fillMaxSize()) {
        if (roomsState.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(200.dp)
            )
            roomScreenViewModel.onUiEvent(RoomsUiEvent.OnLoading)
        } else {
            RoomList(
                modifier = Modifier.fillMaxSize(),
                list = roomsState.roomList,
                onCardClick = onCardClick
            )
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun RoomList(
    modifier: Modifier = Modifier,
    list: MutableList<HotelRoom>,
    onCardClick: (Int) -> Unit
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(6.dp),
        contentPadding = PaddingValues(6.dp),
        modifier = modifier.padding(8.dp)
    ) {
        items(items = list, key = { it.id }) { room ->
            GlideSubcomposition(model = room.roomImage) {
                when (state) {

                    is RequestState.Loading -> CircularProgressIndicator()

                    is RequestState.Failure -> Icon(Icons.Filled.Warning, null)
                    is RequestState.Success -> RoomCard(room = room, onCardClick = onCardClick)
                }
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun RoomCard(room: HotelRoom, onCardClick: (Int) -> Unit, modifier: Modifier = Modifier) {
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
                text = "Тип комнаты: ${room.roomType}",
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}