package ru.hotel.hotel.ui.screens.unauthenticated.login

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hotel.presentation.ui.common.state.ErrorState
import io.ktor.client.call.body
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.hotel.hotel.ui.screens.unauthenticated.login.state.LoginErrorState
import ru.hotel.hotel.ui.screens.unauthenticated.login.state.LoginState
import ru.hotel.hotel.ui.screens.unauthenticated.login.state.LoginUiEvent
import ru.hotel.hotel.ui.screens.unauthenticated.login.state.loginEmptyErrorState
import ru.hotel.hotel.ui.screens.unauthenticated.login.state.passwordEmptyErrorState
import ru.hotel.hotel.ui.screens.unauthenticated.login.state.serverErrorState
import ru.hotel.hotel.ui.screens.unauthenticated.login.state.userNotFoundErrorState
import ru.ktor_koin.network.model.User
import ru.ktor_koin.network.model.UserInfo
import ru.ktor_koin.network.sharedPrefs.SharedPreferencesHelper
import ru.ktor_koin.repositories.LoginRepository

class LoginViewModel(private val loginRepository: LoginRepository) : ViewModel() {
    var loginState = mutableStateOf(LoginState())
        private set

    fun onUiEvent(loginUiEvent: LoginUiEvent) {
        when (loginUiEvent) {
            is LoginUiEvent.LoginChanged -> {
                loginState.value = loginState.value.copy(
                    login = loginUiEvent.inputValue,
                    errorState = loginState.value.errorState.copy(
                        loginErrorState = if (loginUiEvent.inputValue.trim().isNotEmpty()) {
                            ErrorState()
                        } else {
                            loginEmptyErrorState
                        }
                    )
                )
            }

            is LoginUiEvent.PasswordChanged -> {
                loginState.value = loginState.value.copy(
                    password = loginUiEvent.inputValue,
                    errorState = loginState.value.errorState.copy(
                        passwordErrorState = if (loginUiEvent.inputValue.trim().isNotEmpty()) {
                            ErrorState()
                        } else {
                            loginEmptyErrorState
                        }
                    )
                )
            }

            is LoginUiEvent.Submit -> {
                val inputsValidated = validateInputs()
                if (inputsValidated) {
                    loginState.value = loginState.value.copy(isLoading = true)
                    login()
                }
            }

            is LoginUiEvent.OnUserFound -> {
                loginState.value =
                    loginState.value.copy(isLoginSuccessful = true, isLoading = false)
            }

            is LoginUiEvent.OnUserNotFound -> {
                loginState.value = loginState.value.copy(
                    isLoading = false,
                    errorState = loginState.value.errorState.copy(connectionErrorState = userNotFoundErrorState)
                )
            }

            is LoginUiEvent.OnServerError -> {
                loginState.value = loginState.value.copy(
                    isLoading = false,
                    errorState = loginState.value.errorState.copy(connectionErrorState = serverErrorState)
                )
            }
        }
    }

    private fun login() {
        viewModelScope.launch {
            with(loginState.value) {
                val result = withContext(Dispatchers.IO) {
                    loginRepository.loginUser(
                        User(
                            login = login,
                            password = password,
                            admin = false
                        )
                    )
                }
                if (result != null) {
                    when (result.status) {
                        HttpStatusCode.Found -> {
                            onUiEvent(LoginUiEvent.OnUserFound)
                            withContext(Dispatchers.IO) {
                                SharedPreferencesHelper.saveUserId(result.body<UserInfo>().id)
                                SharedPreferencesHelper.saveUserName(result.body<UserInfo>().login)
                                SharedPreferencesHelper.saveUserAdmin(result.body<UserInfo>().admin)
                            }
                        }

                        HttpStatusCode.NotFound -> onUiEvent(LoginUiEvent.OnUserNotFound)
                        HttpStatusCode.InternalServerError -> onUiEvent(LoginUiEvent.OnServerError)
                    }
                } else {
                    onUiEvent(LoginUiEvent.OnServerError)
                }
            }
        }
    }

    private fun validateInputs(): Boolean {
        val loginString = loginState.value.login.trim()
        val passwordString = loginState.value.password
        return when {
            loginString.isEmpty() -> {
                loginState.value = loginState.value.copy(
                    errorState = LoginErrorState(loginErrorState = loginEmptyErrorState)
                )
                false
            }

            passwordString.isEmpty() -> {
                loginState.value = loginState.value.copy(
                    errorState = LoginErrorState(passwordErrorState = passwordEmptyErrorState)
                )
                false
            }

            else -> {
                loginState.value = loginState.value.copy(errorState = LoginErrorState())
                true
            }
        }
    }
}