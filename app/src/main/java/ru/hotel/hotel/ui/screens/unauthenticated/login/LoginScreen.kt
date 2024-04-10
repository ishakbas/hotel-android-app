package ru.hotel.hotel.ui.screens.unauthenticated.login

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel
import ru.hotel.hotel.ui.common.customComposables.MinimalDialog
import ru.hotel.hotel.ui.screens.unauthenticated.login.state.LoginUiEvent
import ru.ktor_koin.network.sharedPrefs.SharedPreferencesHelper
import ru.ktor_koin.network.sharedPrefs.SharedPreferencesHelper.saveIpAddress


@Composable
fun LoginScreen(
    loginViewModel: LoginViewModel = koinViewModel(),
    onNavigateToRegistration: () -> Unit,
    onLogin: () -> Unit,
) {
    val ipAddress = SharedPreferencesHelper.getIpAddress()


    var dialogFieldValue by remember {
        mutableStateOf(ipAddress)
    }

    var dialogState by remember {
        mutableStateOf(false)
    }

    val loginState by remember { loginViewModel.loginState }

    if (loginState.isLoginSuccessful) {
        LaunchedEffect(key1 = Unit) {
            onLogin()
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
                        dialogFieldValue = ipAddress
                    },
                    onConfirmRequest = {
                        saveIpAddress(dialogFieldValue)
                        dialogState = false
                    }
                )
            }
        }
    }
}


