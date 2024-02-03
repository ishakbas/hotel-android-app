package com.example.hotel

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.hotel.data.remote.api.KtorApiClient
import com.example.hotel.presentation.ui.navigation.NavRoutes
import com.example.hotel.presentation.ui.navigation.authenticatedGraph
import com.example.hotel.presentation.ui.navigation.unauthenticatedGraph
import com.example.hotel.ui.theme.HotelTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HotelTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen(this)
                }
            }
        }
    }
}

@Composable
fun MainScreen(owner: ViewModelStoreOwner) {
    val navController = rememberNavController()
    val context = LocalContext.current

    val client = KtorApiClient(context)
    NavigationHost(client, navHostController = navController, owner)
}


@Composable
fun NavigationHost(
    client: KtorApiClient,
    navHostController: NavHostController,
    owner: ViewModelStoreOwner
) {
    NavHost(
        navController = navHostController,
        startDestination = NavRoutes.Unauthenticated.NavigationRoute.route
    ) {
        unauthenticatedGraph(client, navHostController)
        authenticatedGraph(owner)
    }
}