package ru.hotel.hotel.ui.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import ru.hotel.hotel.ui.screens.authenticated.AuthenticatedScreen
import ru.hotel.hotel.ui.screens.unauthenticated.login.LoginScreen
import ru.hotel.hotel.ui.screens.unauthenticated.registration.RegistrationScreen

fun NavGraphBuilder.unauthenticatedGraph(
    onLogin: () -> Unit,
    onNavigateToRegistration: () -> Unit,
    onNavigateToLogin: () -> Unit,
) {
    navigation(
        route = NavRoutes.Unauthenticated.NavigationRoute.route,
        startDestination = NavRoutes.Unauthenticated.Login.route
    ) {
        composable(NavRoutes.Unauthenticated.Login.route) {
            LoginScreen(onNavigateToRegistration = onNavigateToRegistration, onLogin = onLogin)
        }
        composable(NavRoutes.Unauthenticated.Register.route) {
            RegistrationScreen(onNavigateToLogin = onNavigateToLogin)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun NavGraphBuilder.authenticatedGraph() {
    navigation(
        route = NavRoutes.Authenticated.NavigationRoute.route,
        startDestination = NavRoutes.Authenticated.Rooms.route
    ) {
        composable(NavRoutes.Authenticated.AuthScreen.route) {
            AuthenticatedScreen()
        }
    }
}