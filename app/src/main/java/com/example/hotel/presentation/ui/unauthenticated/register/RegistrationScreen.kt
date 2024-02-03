package com.example.hotel.presentation.ui.unauthenticated.register

import android.util.Log
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.hotel.data.TAG
import com.example.hotel.data.remote.User
import com.example.hotel.data.remote.api.KtorApiClient
import com.example.hotel.data.repository.UsersRepository
import com.example.hotel.data.store.DataStoreManager
import com.example.hotel.presentation.ui.unauthenticated.register.state.RegistrationUiEvent
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.launch
@Composable
fun RegistrationScreen(
    ktorApiClient: KtorApiClient,
    registrationViewModel: RegistrationViewModel = viewModel(),
    onNavigateToLogin: () -> Unit,
) {
    val context = LocalContext.current

    val scope = rememberCoroutineScope()
    val repository = UsersRepository(ktorApiClient)

    val registerState by remember { registrationViewModel.registrationState }

    if (registerState.isRegisterSuccessful) {
        LaunchedEffect(key1 = true) {
            onNavigateToLogin.invoke()
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
                text = "Регистрация",
                style = MaterialTheme.typography.displayMedium
            )

            RegistrationInputs(
                modifier = Modifier.align(Alignment.Center),
                registrationState = registerState,
                onLoginChange = {
                    registrationViewModel.onUiEvent(
                        registrationUiEvent = RegistrationUiEvent.LoginChanged(
                            it
                        )
                    )
                },
                onPasswordChange = {
                    registrationViewModel.onUiEvent(
                        registrationUiEvent = RegistrationUiEvent.PasswordChanged(
                            it
                        )
                    )
                },
                onSubmit = {
                    registrationViewModel.onUiEvent(registrationUiEvent = RegistrationUiEvent.Submit)
                    scope.launch {
                        val response =
                            repository.register(
                                user = User(
                                    registerState.login,
                                    registerState.password
                                )
                            )
                        Log.d(TAG, "$response")
                        when (response) {
                            HttpStatusCode.Created -> {
                                registrationViewModel.onUiEvent(registrationUiEvent = RegistrationUiEvent.OnUserCreate)
                                ktorApiClient.closeClient()
                                Toast.makeText(context, "Пользователь создан", Toast.LENGTH_LONG)
                                    .show()
                            }

                            HttpStatusCode.Conflict -> {
                                registrationViewModel.onUiEvent(registrationUiEvent = RegistrationUiEvent.OnUserNotCreate)
                                Toast.makeText(
                                    context,
                                    "Пользователь уже существует",
                                    Toast.LENGTH_LONG
                                ).show()
                            }

                            else -> {
                                registrationViewModel.onUiEvent(registrationUiEvent = RegistrationUiEvent.OnServerError)
                                Toast.makeText(
                                    context,
                                    "Ошибка сервера, $response",
                                    Toast.LENGTH_LONG
                                ).show()
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
                Text(text = "У Вас есть аккаунт?", style = MaterialTheme.typography.labelLarge)
                ClickableText(
                    text = AnnotatedString("Войти"),
                    style = TextStyle(
                        color = MaterialTheme.colorScheme.primary
                    ),
                    onClick = {
                        onNavigateToLogin.invoke()
                    }
                )
            }
        }
    }
}