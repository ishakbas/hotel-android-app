package ru.ktor_koin.repositories

import io.ktor.http.HttpStatusCode
import ru.ktor_koin.network.KtorClient
import ru.ktor_koin.network.model.HotelRoom
import ru.ktor_koin.network.model.HotelRoomsWithTypesExtended
import ru.ktor_koin.network.model.Rent

interface RoomsRepository {
    suspend fun loadRooms(): List<HotelRoom>
    suspend fun loadRoom(id: Int): HotelRoomsWithTypesExtended
    suspend fun rentRoom(rent: Rent): HttpStatusCode
}

class RoomsRepositoryImpl(private val ktorClient: KtorClient) : RoomsRepository {
    override suspend fun loadRooms(): List<HotelRoom> {
        return ktorClient.loadRooms()
    }

    override suspend fun loadRoom(id: Int): HotelRoomsWithTypesExtended {
        return ktorClient.loadRoom(id = id)
    }

    override suspend fun rentRoom(rent: Rent): HttpStatusCode {
        return ktorClient.rentRoom(rent)
    }
}