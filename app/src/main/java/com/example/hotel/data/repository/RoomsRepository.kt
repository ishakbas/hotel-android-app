package com.example.hotel.data.repository

import com.example.hotel.data.remote.api.KtorApiClient
import com.example.hotel.data.remote.HotelRoom

class RoomsRepository(private val client: KtorApiClient) {
    suspend fun fetchRooms(): MutableList<HotelRoom> {
        return client.getRooms()
    }
}
