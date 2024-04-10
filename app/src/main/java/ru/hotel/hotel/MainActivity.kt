package ru.hotel.hotel

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import ru.hotel.hotel.ui.HotelApp
import ru.hotel.hotel.ui.theme.HotelTheme
import ru.ktor_koin.network.sharedPrefs.SharedPreferencesHelper
import ru.ktor_koin.network.sharedPrefs.SharedPreferencesHelper.IP_ADDRESS

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        SharedPreferencesHelper.init(IP_ADDRESS, this)
        setContent {
            HotelTheme {
                Scaffold { innerPadding ->
                    HotelApp(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}
