package ru.ktor_koin.network.model

import kotlinx.serialization.Serializable

@Serializable
data class User(val login: String, val password: String, val admin: Boolean = false)

@Serializable
data class UserInfo(val id: Int, val login: String, val password: String, val admin: Boolean)
