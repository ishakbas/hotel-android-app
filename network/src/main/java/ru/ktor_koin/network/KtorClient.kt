package ru.ktor_koin.network

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import ru.ktor_koin.network.model.HotelRoom
import ru.ktor_koin.network.model.HotelRoomsWithTypesExtended
import ru.ktor_koin.network.model.Rent
import ru.ktor_koin.network.model.User
import ru.ktor_koin.network.sharedPrefs.SharedPreferencesHelper

interface KtorClient {

    val client: HttpClient

    suspend fun login(user: User): HttpResponse?
    suspend fun register(user: User): HttpStatusCode
    suspend fun loadRooms(): MutableList<HotelRoom>
    suspend fun loadRoom(id: Int): HotelRoomsWithTypesExtended
    suspend fun rentRoom(rent: Rent): HttpStatusCode
}


class KtorClientImpl : KtorClient {

    companion object {
        private const val USER = "/user/"
        private const val ROOM = "/room/"
        const val USER_LOGIN = "${USER}login"
        const val USER_REGISTER = "${USER}register"
        const val ROOMS = "${ROOM}all"
        const val BOOKING = "/booking/"
        const val TAG = "my"
    }

    override val client = HttpClient {
        install(ContentNegotiation) {
            json(json = Json { ignoreUnknownKeys = true })
        }
    }

    override suspend fun login(user: User): HttpResponse? {
        return try {
            client.post("${SharedPreferencesHelper.getIpAddress()}$USER_LOGIN") {
                setBody(user)
                contentType(ContentType.Application.Json)
            }
        } catch (ex: Exception) {
            Log.d(TAG, ex.toString())
            null
        }
    }

    override suspend fun register(user: User): HttpStatusCode {
        return try {
            client.post("${SharedPreferencesHelper.getIpAddress()}$USER_REGISTER") {
                setBody(user)
                contentType(ContentType.Application.Json)
            }.status
        } catch (ex: Exception) {
            Log.d(TAG, ex.toString())
            HttpStatusCode.InternalServerError
        }
    }

    override suspend fun loadRooms(): MutableList<HotelRoom> {
        return try {
            client.get("${SharedPreferencesHelper.getIpAddress()}$ROOMS")
                .body<MutableList<HotelRoom>>()
        } catch (ex: Exception) {
            Log.d(TAG, ex.toString())
            mutableListOf()
        }
    }

    override suspend fun loadRoom(id: Int): HotelRoomsWithTypesExtended {
        return try {
            client.get("${SharedPreferencesHelper.getIpAddress()}$ROOM$id")
                .body<HotelRoomsWithTypesExtended>()
        } catch (ex: Exception) {
            HotelRoomsWithTypesExtended()
        }
    }

    override suspend fun rentRoom(rent: Rent): HttpStatusCode {
        return try {
            client.post("${SharedPreferencesHelper.getIpAddress()}$BOOKING") {
                setBody(rent)
                contentType(ContentType.Application.Json)
            }.status
        } catch (ex: Exception) {
            HttpStatusCode.InternalServerError
        }
    }
}