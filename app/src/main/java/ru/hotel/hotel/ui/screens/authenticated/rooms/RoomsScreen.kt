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
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import ru.hotel.hotel.ui.screens.authenticated.rooms.state.RoomsUiEvent
import ru.ktor_koin.network.model.HotelRoom

@Composable
fun RoomsScreen(
    modifier: Modifier = Modifier,
    roomScreenViewModel: RoomsViewModel,
    searchState: MutableState<String>,
    onCardClick: (Int) -> Unit
) {
    val roomsState by roomScreenViewModel.roomsState.collectAsState()
    val searchedText = searchState.value

    val filteredRooms: List<HotelRoom> =
        if (searchedText.isEmpty()) roomsState.roomList else {
            val resultList: ArrayList<HotelRoom> = ArrayList()

            for (room in roomsState.roomList) {
                if (room.name.lowercase(java.util.Locale.ROOT)
                        .contains(searchedText.lowercase(java.util.Locale.ROOT))
                ) {
                    resultList.add(room)
                }
            }
            resultList
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
                list = filteredRooms,
                onCardClick = onCardClick
            )
        }
    }
}

@Composable
fun RoomList(
    modifier: Modifier = Modifier,
    list: List<HotelRoom>,
    onCardClick: (Int) -> Unit
) {
    val context = LocalContext.current
    val requestManager = Glide.with(context)

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(6.dp),
        contentPadding = PaddingValues(6.dp),
        modifier = modifier.padding(8.dp)
    ) {
        items(items = list) { room ->
            RoomCard(item = room, onCardClick = onCardClick, requestManager = requestManager)
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun RoomCard(
    item: HotelRoom,
    onCardClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
    requestManager: RequestManager
) {
    Card(modifier = modifier
        .fillMaxWidth()
        .heightIn(min = 150.dp, max = 300.dp)
        .clickable {
            onCardClick(item.id)
        }) {
        Column(
            Modifier
                .padding(10.dp)
                .fillMaxWidth()
        ) {
            GlideImage(
                model = item.roomImage,
                modifier = Modifier
                    .fillMaxWidth()
                    .size(200.dp)
                    .clip(RoundedCornerShape(6.dp)),
                contentScale = ContentScale.FillBounds,
                contentDescription = null
            ) {
                it
                    .thumbnail(
                        requestManager
                            .asDrawable()
                            .load(item.roomImage)
                            .override(400)
                    )
            }
            Text(text = item.name, style = MaterialTheme.typography.headlineMedium)
            Text(
                text = "Тип комнаты: ${item.roomType}",
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}