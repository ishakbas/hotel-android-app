package ru.hotel.hotel.ui.screens.authenticated.profile

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ru.ktor_koin.network.sharedPrefs.SharedPreferencesHelper

@Composable
fun ProfileScreen(profileScreenViewModel: ProfileScreenViewModel, modifier: Modifier = Modifier) {
    val uploadRoomState = profileScreenViewModel.state.collectAsState()

    val userName = SharedPreferencesHelper.getUserName() ?: "nickname"
    val userAdmin = SharedPreferencesHelper.getUserAdmin()

    Column {
        UserInfoBox(userName = userName, userAdmin = userAdmin)
//        AddHotelDialog()
    }
}

@Composable
private fun UserInfoBox(userName: String, userAdmin: Boolean) {
    Column {
        Box(
            modifier = Modifier
                .size(400.dp)
                .padding(24.dp)
        ) {
            Icon(
                imageVector = Icons.Outlined.Person,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .size(250.dp)
                    .align(Alignment.TopCenter)
            )
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = userName,
                    style = MaterialTheme.typography.displayMedium,
                    fontWeight = FontWeight.Bold
                )
                if (userAdmin) {
                    Text(
                        text = if (userAdmin) "Администратор" else "",
                        style = MaterialTheme.typography.headlineMedium
                    )
                }
            }
        }
    }
}

/*@Preview(showSystemUi = true)
@Composable
private fun AddHotelDialog(modifier: Modifier = Modifier) {
    val dialogState = remember {
        mutableStateOf(true)
    }

    Button(
        onClick = { dialogState.value = true },
        modifier = Modifier
            .fillMaxWidth()
            .padding(6.dp)
    ) {
        Text(text = "Загрузить новую комнату")
    }

    if (dialogState.value) {
        Dialog(onDismissRequest = { dialogState.value = false }) {
            Surface(
                tonalElevation = 6.dp,
                modifier = Modifier.padding(6.dp),
                shape = MaterialTheme.shapes.extraLarge
            ) {
//                EditHotel()
            }
        }
    }
}*/

/*

@Composable
fun EditHotel(modifier: Modifier = Modifier) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        var selectedImageUri by remember { mutableStateOf<Uri>(Uri.parse("")) }
        var imageByteArray by remember {
            mutableStateOf<ByteArray?>(null)
        }

        val context = LocalContext.current
        val photoPicker = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.PickVisualMedia(),
            onResult = { uri ->
                run {
                    if (uri != null) {
                        selectedImageUri = uri
                    }
                    GlobalScope.launch {
                        imageByteArray = convertToBytes(selectedImageUri, context)
                    }
                }
            }
        )

        var roomName by remember { mutableStateOf("") }
        var roomTypeId by remember { mutableStateOf("") }

        val scope = rememberCoroutineScope()

        LazyColumn(Modifier.fillMaxSize()) {
            item {
                Column(
                    Modifier.fillMaxWidth(),
                ) {
                    TextField(value = roomName, onValueChange = { roomName = it })
                    TextField(
                        value = roomTypeId,
                        onValueChange = { roomTypeId = it })

                    Button(onClick = {
                        photoPicker.launch(
                            PickVisualMediaRequest(
                                ActivityResultContracts.PickVisualMedia.ImageOnly
                            )
                        )
                    }) {
                        Text(text = "choose image")
                    }
                    Button(onClick = {
//                        scope.launch {
//                            hotelService.postHotelRoom(
//                                HotelRooms(
//                                    roomName,
//                                    roomTypeId.toInt(),
//                                    imageByteArray
//                                )
//                            )
//                        }
                    }) {
                        Text(text = "send hotel")
                    }
                }
            }
        }
    }
}*/
