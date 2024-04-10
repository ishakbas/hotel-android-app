package ru.hotel.hotel.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Home
import com.example.hotel.presentation.ui.navigation.BarItem

object NavItems {
    val BarItems = listOf(
        BarItem(
            title = "Комнаты",
            icon = Icons.Filled.Home,
            route = NavRoutes.Authenticated.Rooms.route
        ),
        BarItem(
            title = "Профиль",
            icon = Icons.Filled.Face,
            route = NavRoutes.Authenticated.Profile.route
        )
    )
}