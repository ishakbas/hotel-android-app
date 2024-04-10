package ru.hotel.hotel.ui.screens.unauthenticated.login

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Login
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Password
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ru.hotel.hotel.ui.common.customComposables.CustomOutlinedButton
import ru.hotel.hotel.ui.common.customComposables.CustomTextField
import ru.hotel.hotel.ui.screens.unauthenticated.login.state.LoginState

@Composable
fun LoginInputs(
    loginState: LoginState,
    onLoginChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onSubmit: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = modifier.fillMaxWidth()) {
        Icon(
            imageVector = Icons.Filled.Home,
            contentDescription = null,
            modifier = Modifier
                .size(128.dp)
                .padding(8.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        CustomTextField(
            label = "Логин",
            value = loginState.login,
            onValueChange = onLoginChange,
            leadingIcon = Icons.AutoMirrored.Filled.Login,
            isError = loginState.errorState.loginErrorState.hasError,
            errorText = loginState.errorState.loginErrorState.errorMessageStringResource,
        )

        CustomTextField(
            label = "Пароль",
            value = loginState.password,
            onValueChange = onPasswordChange,
            leadingIcon = Icons.Filled.Password,
            isError = loginState.errorState.passwordErrorState.hasError,
            errorText = loginState.errorState.passwordErrorState.errorMessageStringResource,
            password = true
        )

        CustomOutlinedButton(
            label = "Войти",
            enabled = !loginState.isLoading,
            onClickAction = onSubmit,
            modifier = Modifier.padding(12.dp)
        )
        if (loginState.errorState.connectionErrorState.hasError) {
            Text(
                text = loginState.errorState.connectionErrorState.errorMessageStringResource,
                style = MaterialTheme.typography.bodySmall.copy(color = Color.Red)
            )
        }
    }
}