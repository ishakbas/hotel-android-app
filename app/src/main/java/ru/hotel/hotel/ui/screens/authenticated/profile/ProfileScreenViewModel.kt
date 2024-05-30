package ru.hotel.hotel.ui.screens.authenticated.profile

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.hotel.hotel.ui.screens.authenticated.profile.state.HotelUploadState
import ru.hotel.hotel.ui.screens.authenticated.profile.state.ProfileScreenEvents

class ProfileScreenViewModel : ViewModel() {
    val state = MutableStateFlow(HotelUploadState())

    init {
        onUiEvent(ProfileScreenEvents.START)
    }

    fun onUiEvent(event: ProfileScreenEvents) {
        when (event) {
            ProfileScreenEvents.START -> {}

            ProfileScreenEvents.LOADING -> state.value = state.value.copy(loading = true)

            ProfileScreenEvents.SUCCESS -> TODO()

            ProfileScreenEvents.ERROR -> TODO()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun convertToBytes(imgPath: Uri, context: Context) = viewModelScope.launch {

        val persistable =
            Intent.FLAG_GRANT_READ_URI_PERMISSION.and(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)

        context.contentResolver.takePersistableUriPermission(
            imgPath,
            persistable
        )

        val byteArray: ByteArray?
        val stream = context.contentResolver.openInputStream(imgPath)
        withContext(Dispatchers.IO) {
            byteArray = stream?.readBytes()
            stream?.close()
        }
    }
}

