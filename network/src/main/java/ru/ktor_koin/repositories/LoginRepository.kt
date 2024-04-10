package ru.ktor_koin.repositories

import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode
import ru.ktor_koin.network.KtorClient
import ru.ktor_koin.network.model.User

interface LoginRepository {
    suspend fun loginUser(user: User): HttpResponse
    suspend fun registerUser(user: User): HttpStatusCode
}

class LoginRepositoryImpl(private val ktorClient: KtorClient) : LoginRepository {
    override suspend fun loginUser(user: User): HttpResponse {
        return this.ktorClient.login(user)?.body()!!
    }

    override suspend fun registerUser(user: User): HttpStatusCode {
        return ktorClient.register(user)
    }
}