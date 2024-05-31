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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import ru.hotel.hotel.ui.screens.authenticated.profile.ProfileScreenViewModel
import ru.hotel.hotel.ui.screens.authenticated.rent.RentScreen
import ru.hotel.hotel.ui.screens.authenticated.rent.RentViewModel
import ru.hotel.hotel.ui.screens.authenticated.rooms.RoomsScreen
import ru.hotel.hotel.ui.screens.authenticated.rooms.RoomsViewModel
import ru.ktor_koin.network.sharedPrefs.SharedPreferencesHelper

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AuthenticatedScreen(
    roomsViewModel: RoomsViewModel = koinViewModel(),
    rentViewModel: RentViewModel = koinViewModel(),
    profileScreenViewModel: ProfileScreenViewModel = koinViewModel()
) {
    val navController = rememberNavController()
    val searchTextState = remember {
        mutableStateOf("")
    }
    Scaffold(
        topBar = {
            TopBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                searchState = searchTextState
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
                    searchState = searchTextState,
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
                ProfileScreen(profileScreenViewModel)
            }
        }
    }
}

@Composable
private fun TopBar(modifier: Modifier = Modifier, searchState: MutableState<String>) {

    val textFieldVisibility = remember {
        mutableStateOf(false)
    }

    Box(modifier) {
        if (textFieldVisibility.value) {
            TextField(
                value = searchState.value,
                onValueChange = { value -> searchState.value = value },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,

                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null
                    )
                },
                trailingIcon = {
                    IconButton(onClick = {
                        searchState.value = ""
                        textFieldVisibility.value = false
                    }) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = null)
                    }

                }
            )
        } else {

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
                IconButton(onClick = { textFieldVisibility.value = true }) {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = null,
                        modifier = Modifier.size(32.dp)
                    )
                }
                Icon(
                    imageVector = Icons.Filled.FilterAlt,
                    contentDescription = null,
                    modifier = Modifier.size(32.dp)
                )
            }
        }
    }
}