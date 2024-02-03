package com.example.hotel.presentation.ui.authenticated

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.hotel.data.remote.UserInfo
import com.example.hotel.data.remote.api.KtorApiClient
import com.example.hotel.data.repository.RoomsRepository
import com.example.hotel.data.store.DataStoreManager
import com.example.hotel.presentation.ui.authenticated.profile.ProfileScreen
import com.example.hotel.presentation.ui.authenticated.room.RoomDetail
import com.example.hotel.presentation.ui.authenticated.rooms.RoomsScreen
import com.example.hotel.presentation.ui.authenticated.rooms.RoomsViewModel
import com.example.hotel.presentation.ui.navigation.ID_KEY
import com.example.hotel.presentation.ui.navigation.NavRoutes
import com.example.navbarexample.presentation.ui.navigation.NavItems

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AuthenticatedScreen(owner: ViewModelStoreOwner) {
    val navController = rememberNavController()

    val context = LocalContext.current
    val ktorApiClient = KtorApiClient(context)
    val dataStoreManager = DataStoreManager(context)

    val userValue = remember {
        mutableStateOf(UserInfo(0, "Неизвестный", "", false))
    }
    LaunchedEffect(key1 = true) {
        dataStoreManager.getUserInfo().collect { data -> userValue.value = data }
    }

//    val roomsRepository = RoomsRepository(ktorApiClient)
////    val roomsViewModelFactory = RoomsViewModel.HotelRoomViewModelFactory(roomsRepository)
//////    val roomsViewModel =
//////        ViewModelProvider(owner, roomsViewModelFactory)[RoomsViewModel::class.java]

    Scaffold(
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
//                RoomsScreen(
//                    roomsViewModel = roomsViewModel,
//                    userValue.value
//                ) { receivedId ->
//                    navController.navigate(NavRoutes.Authenticated.Room.passId(receivedId)) {
//                        launchSingleTop = true
//                    }
//                }
            }
            composable(
                route = NavRoutes.Authenticated.Room.route,
                arguments = listOf(navArgument(ID_KEY) { type = NavType.IntType })
            ) { backStackEntry ->
                val id = backStackEntry.arguments?.getInt(ID_KEY) ?: 0
                RoomDetail(roomId = id, ktorApiClient = ktorApiClient, userInfo = userValue.value)
            }
            composable(NavRoutes.Authenticated.Profile.route) {
                ProfileScreen(userValue.value)
            }
        }
    }
}