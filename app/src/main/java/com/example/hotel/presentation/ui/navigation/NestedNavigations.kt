package com.example.hotel.presentation.ui.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.hotel.data.remote.api.KtorApiClient
import com.example.hotel.presentation.ui.unauthenticated.register.RegistrationScreen
import com.example.hotel.presentation.ui.authenticated.AuthenticatedScreen
import com.example.navbarexample.presentation.ui.unauthenticated.login.LoginScreen


fun NavGraphBuilder.unauthenticatedGraph(client: KtorApiClient, navController: NavController) {
    navigation(
        route = NavRoutes.Unauthenticated.NavigationRoute.route,
        startDestination = NavRoutes.Unauthenticated.Login.route
    ) {
        composable(NavRoutes.Unauthenticated.Login.route) {
            LoginScreen(
                ktorApiClient = client,
                onNavigateToAuthenticatedRoute = { navController.navigate(NavRoutes.Authenticated.AuthScreen.route) },
                onNavigateToRegistration = {
                    navController.navigate(NavRoutes.Unauthenticated.Register.route) {
                        popUpTo(NavRoutes.Unauthenticated.Login.route)
                    }
                })
        }
        composable(NavRoutes.Unauthenticated.Register.route) {
            RegistrationScreen(
                ktorApiClient = client
            ) {
                navController.navigate(NavRoutes.Unauthenticated.Login.route) {
                    popUpTo(NavRoutes.Unauthenticated.Register.route)
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun NavGraphBuilder.authenticatedGraph(owner: ViewModelStoreOwner) {
    navigation(
        route = NavRoutes.Authenticated.NavigationRoute.route,
        startDestination = NavRoutes.Authenticated.Rooms.route
    ) {
        composable(route = NavRoutes.Authenticated.AuthScreen.route) {
            AuthenticatedScreen(owner)
        }
    }
}

