package ru.hotel.hotel.ui.screens.unauthenticated.registration

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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel
import ru.hotel.hotel.ui.screens.unauthenticated.registration.state.RegistrationUiEvent

@Composable
fun RegistrationScreen(
    registrationViewModel: RegistrationViewModel = koinViewModel(),
    onNavigateToLogin: () -> Unit,
) {

    val registerState by remember { registrationViewModel.registrationState }

    if (registerState.isRegisterSuccessful) {
        LaunchedEffect(key1 = Unit) {
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