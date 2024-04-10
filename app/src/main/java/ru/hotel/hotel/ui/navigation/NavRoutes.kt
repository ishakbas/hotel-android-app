package ru.hotel.hotel.ui.navigation

const val ID_KEY = "id"

sealed class NavRoutes {
    sealed class Unauthenticated(val route: String) : NavRoutes() {
        data object NavigationRoute : Unauthenticated(route = "unauthenticated")
        data object Login : Unauthenticated("login")
        data object Register : Unauthenticated("register")
    }

    sealed class Authenticated(val route: String) : NavRoutes() {
        data object SomeScreen : Authenticated(route = "someScreen")
        data object NavigationRoute : Authenticated(route = "authenticated")
        data object AuthScreen : Authenticated("authScreen")
        data object Rooms : Authenticated("rooms")
        data object Room : Authenticated("room/{$ID_KEY}") {
            fun passId(id: Int): String {
                return "room/$id"
            }
        }

        data object Profile : Authenticated("profile")
    }
}