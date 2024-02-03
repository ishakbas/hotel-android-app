package com.example.hotel.data.remote.api

import android.content.Context
import android.util.Log
import com.example.hotel.data.TAG
import com.example.hotel.data.remote.User
import com.example.hotel.data.store.DataStoreManager
import com.example.hotel.data.remote.HotelRoom
import com.example.navbarexample.data.remote.Rent
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.engine.cio.endpoint
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class KtorApiClient(context: Context) {
    private val dataStore = DataStoreManager(context = context)

    private lateinit var baseUrl: String

    init {
        CoroutineScope(Dispatchers.IO).launch {
            baseUrl = "http://${dataStore.getIpAddress.first()}:8080"
        }
    }

    private val client = HttpClient(CIO) {
        engine {
            endpoint {
                connectTimeout = 700
                connectAttempts = 2
            }
        }
        install(ContentNegotiation) {
            json()
        }
    }

    companion object {
        private const val USER = "/user/"
        const val USER_LOGIN = "${USER}login"
        const val USER_REGISTER = "${USER}register"
        const val ROOM = "/room/"
        const val ROOMS = "${ROOM}all"
        const val BOOKING = "/booking/"
    }

    fun closeClient() = client.close()
    suspend fun userLogin(user: User): HttpResponse? {
        return try {
            client.post("$baseUrl$USER_LOGIN") {
                setBody(user)
                contentType(ContentType.Application.Json)
            }
        } catch (ex: Exception) {
            Log.d("my", ex.toString())
            null
        }
    }

    suspend fun userRegister(user: User): HttpStatusCode {
        return try {
            client.post("$baseUrl$USER_REGISTER") {
                setBody(user)
                contentType(ContentType.Application.Json)
            }.status
        } catch (ex: Exception) {
            Log.d("my", ex.toString())
            HttpStatusCode.InternalServerError
        }
    }

    suspend fun getRooms(): MutableList<HotelRoom> {
        return try {
            client.get("$baseUrl$ROOMS").body<MutableList<HotelRoom>>()
        } catch (ex: Exception) {
            Log.d("my", ex.toString())
            mutableListOf()
        }
    }

    suspend fun getRoom(id: Int): HotelRoom {
        return try {
            client.get("$baseUrl$ROOM$id").body<HotelRoom>()
        } catch (ex: Exception) {
            Log.d("my", ex.toString())
            HotelRoom(0, "", "", ByteArray(0))
        }
    }

    suspend fun rentRoom(rent: Rent): HttpStatusCode {
        return try {
            client.post("$baseUrl$BOOKING") {
                setBody(rent)
                contentType(ContentType.Application.Json)
            }.status
            HttpStatusCode.Created
        } catch (ex: Exception) {
            Log.d(TAG, "rentRoom: $ex")
            HttpStatusCode.BadRequest
        }
    }
}
