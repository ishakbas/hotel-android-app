package com.example.navbarexample.presentation.ui.unauthenticated.login

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.hotel.data.remote.User
import com.example.hotel.data.remote.UserInfo
import com.example.hotel.data.remote.api.KtorApiClient
import com.example.hotel.data.repository.UsersRepository
import com.example.hotel.data.store.DataStoreManager
import com.example.hotel.presentation.ui.common.customComposables.MinimalDialog
import com.example.hotel.presentation.ui.unauthenticated.login.LoginInputs
import com.example.hotel.presentation.ui.unauthenticated.login.LoginViewModel
import com.example.hotel.presentation.ui.unauthenticated.login.state.LoginUiEvent
import io.ktor.client.call.body
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.launch


@Composable
fun LoginScreen(
    ktorApiClient: KtorApiClient,
    loginViewModel: LoginViewModel = viewModel(),
    onNavigateToRegistration: () -> Unit,
    onNavigateToAuthenticatedRoute: () -> Unit,
) {
    val context = LocalContext.current

    var dialogFieldValue by remember {
        mutableStateOf("")
    }

    var dialogState by remember {
        mutableStateOf(false)
    }

    val dataStoreManager = DataStoreManager(context)
    val ipAddress =
        dataStoreManager.getIpAddress.collectAsState(initial = dataStoreManager.getIpAddress)

    val scope = rememberCoroutineScope()
    val repository = UsersRepository(ktorApiClient)

    val loginState by remember { loginViewModel.loginState }

    if (loginState.isLoginSuccessful) {
        LaunchedEffect(key1 = true) {
            onNavigateToAuthenticatedRoute.invoke()
        }
    } else {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {
            Text(
                modifier = Modifier
                    .align(Alignment.TopStart),
                text = "Вход",
                style = MaterialTheme.typography.displayMedium
            )

            LoginInputs(
                modifier = Modifier.align(Alignment.Center),
                loginState = loginState,
                onLoginChange = {
                    loginViewModel.onUiEvent(
                        loginUiEvent = LoginUiEvent.LoginChanged(
                            it
                        )
                    )
                },
                onPasswordChange = {
                    loginViewModel.onUiEvent(
                        loginUiEvent = LoginUiEvent.PasswordChanged(
                            it
                        )
                    )
                },
                onSubmit = {
                    loginViewModel.onUiEvent(loginUiEvent = LoginUiEvent.Submit)
                    scope.launch {
                        val response =
                            repository.login(user = User(loginState.login, loginState.password))
                        if (response != null) {
                            when (response.status) {
                                HttpStatusCode.Found -> {
                                    loginViewModel.onUiEvent(loginUiEvent = LoginUiEvent.OnUserFound)
                                    dataStoreManager.saveUserInfo(response.body<UserInfo>())
                                    ktorApiClient.closeClient()

                                    Toast.makeText(
                                        context,
                                        "Пользователь найден",
                                        Toast.LENGTH_LONG
                                    )
                                        .show()
                                }

                                HttpStatusCode.NotFound -> {
                                    loginViewModel.onUiEvent(loginUiEvent = LoginUiEvent.OnUserNotFound)
                                    Toast.makeText(
                                        context,
                                        "Пользователь не найден",
                                        Toast.LENGTH_LONG
                                    )
                                        .show()
                                }

                                else -> {
                                    Toast.makeText(
                                        context,
                                        "Ошибка сервера, $response",
                                        Toast.LENGTH_LONG
                                    ).show()
                                    loginViewModel.onUiEvent(loginUiEvent = LoginUiEvent.OnServerError)
                                }
                            }
                        }
                    }
                }
            )


            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(20.dp)
            ) {
                Text(text = "Нет аккаунта?", style = MaterialTheme.typography.labelLarge)
                ClickableText(
                    text = AnnotatedString("Создать"),
                    style = TextStyle(
                        color = MaterialTheme.colorScheme.primary
                    ),
                    onClick = {
                        onNavigateToRegistration.invoke()
                    }
                )
            }
            ClickableText(
                text = AnnotatedString("Изменить IP"),
                style = TextStyle(
                    color = MaterialTheme.colorScheme.primary
                ),
                onClick = {
                    dialogState = true
                },
                modifier = Modifier.align(Alignment.BottomEnd)
            )
            if (dialogState) {
                MinimalDialog(
                    fieldValue = dialogFieldValue,
                    onValueChange = { dialogFieldValue = it },
                    onDismissRequest = {
                        dialogState = false
                        dialogFieldValue = ipAddress.value.toString()
                    },
                    onConfirmRequest = {
                        scope.launch {
                            dataStoreManager.saveIpAddress(dialogFieldValue)
                            dialogState = false
                        }
                    }
                )
            }
        }
    }
}