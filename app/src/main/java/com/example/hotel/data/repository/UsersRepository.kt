package com.example.hotel.data.repository

import com.example.hotel.data.remote.User
import com.example.hotel.data.remote.api.KtorApiClient
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode

class UsersRepository(private val client: KtorApiClient) {
    suspend fun login(user: User): HttpResponse? {
        return client.userLogin(user)
    }

    suspend fun register(user: User): HttpStatusCode {
        return client.userRegister(user)
    }
}