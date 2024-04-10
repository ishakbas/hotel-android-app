package ru.hotel.hotel.ui.screens.authenticated

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import org.koin.androidx.compose.koinViewModel
import ru.hotel.hotel.ui.navigation.ID_KEY
import ru.hotel.hotel.ui.navigation.NavItems
import ru.hotel.hotel.ui.navigation.NavRoutes
import ru.hotel.hotel.ui.screens.authenticated.profile.ProfileScreen
import ru.hotel.hotel.ui.screens.authenticated.rent.RentScreen
import ru.hotel.hotel.ui.screens.authenticated.rent.RentViewModel
import ru.hotel.hotel.ui.screens.authenticated.rooms.RoomsScreen
import ru.hotel.hotel.ui.screens.authenticated.rooms.RoomsViewModel
import ru.ktor_koin.network.sharedPrefs.SharedPreferencesHelper

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AuthenticatedScreen(
    roomsViewModel: RoomsViewModel = koinViewModel(),
    rentViewModel: RentViewModel = koinViewModel()
) {
    val navController = rememberNavController()

    Scaffold(
        topBar = {
            TopBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            )
        },
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                NavItems.BarItems.forEach { barItem ->
                    NavigationBarItem(
                        icon = { Icon(barItem.icon, null) },
                        selected = currentDestination?.hierarchy?.any { it.route == barItem.route } == true,
                        alwaysShowLabel = false,
                        label = {
                            Text(
                                text = barItem.title
                            )
                        },
                        onClick = {
                            navController.navigate(barItem.route) {
                                launchSingleTop = true
                                popUpTo(barItem.route)
                            }
                        },
                    )
                }
            }
        }
    ) {
        NavHost(
            navController = navController,
            startDestination = NavRoutes.Authenticated.Rooms.route,
            Modifier.padding(it)
        ) {
            composable(NavRoutes.Authenticated.Rooms.route) {
                RoomsScreen(
                    roomScreenViewModel = roomsViewModel,
                    modifier = Modifier.fillMaxSize()
                ) { receivedId ->
                    navController.navigate(NavRoutes.Authenticated.Room.passId(receivedId)) {
                        launchSingleTop = true
                    }
                }
            }
            composable(
                route = NavRoutes.Authenticated.Room.route,
                arguments = listOf(navArgument(ID_KEY) { type = NavType.IntType })
            ) { backStackEntry ->
                val id = backStackEntry.arguments?.getInt(ID_KEY) ?: 0
                SharedPreferencesHelper.saveRoomId(id)
                RentScreen(
                    id = id,
                    rentViewModel = rentViewModel,
                    modifier = Modifier.padding(16.dp)
                )
            }
            composable(NavRoutes.Authenticated.Profile.route) {
                ProfileScreen()
            }
        }
    }
}

@Composable
private fun TopBar(modifier: Modifier = Modifier) {
    Box(modifier) {
        Icon(
            imageVector = Icons.AutoMirrored.Outlined.Logout,
            contentDescription = null,
            modifier = Modifier
                .align(
                    Alignment.CenterStart
                )
                .size(32.dp)
        )
        Text(
            text = "Номера",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.align(Alignment.Center)
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.align(
                Alignment.CenterEnd
            )
        ) {
            Icon(
                imageVector = Icons.Filled.Search,
                contentDescription = null,
                modifier = Modifier.size(32.dp)
            )
            Icon(
                imageVector = Icons.Filled.FilterAlt,
                contentDescription = null,
                modifier = Modifier.size(32.dp)
            )
        }
    }
}