package ru.hotel.hotel.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import ru.hotel.hotel.ui.navigation.NavRoutes
import ru.hotel.hotel.ui.navigation.authenticatedGraph
import ru.hotel.hotel.ui.navigation.unauthenticatedGraph

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HotelApp(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    Box(modifier = modifier.fillMaxSize()) {
        NavHost(
            navController = navController,
            startDestination = NavRoutes.Unauthenticated.NavigationRoute.route,
        ) {
            unauthenticatedGraph(
                onNavigateToLogin = {
                    navController.navigate(NavRoutes.Unauthenticated.Login.route)
                },
                onNavigateToRegistration = {
                    navController.navigate(NavRoutes.Unauthenticated.Register.route) {
                        popUpTo(NavRoutes.Unauthenticated.Login.route)
                        launchSingleTop = true
                    }
                },
                onLogin = {
                    navController.navigate(NavRoutes.Authenticated.AuthScreen.route) { popUpTo(0) }
                },
                onRegister = {},
            )
            authenticatedGraph()
        }
    }
}
