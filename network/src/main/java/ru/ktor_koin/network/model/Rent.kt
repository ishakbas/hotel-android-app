package ru.ktor_koin.network.model

import kotlinx.datetime.LocalDate
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Rent(
    @SerialName("user_id")
    val userId: Int = 0,
    @SerialName("room_id")
    val roomId: Int = 0,
    @SerialName("check_in_date")
    val checkInDate: LocalDate = LocalDate(2024, 1, 1),
    @SerialName("check_out_date")
    val checkOutDate: LocalDate = LocalDate(2024, 1, 1),
    val status: String? = null
)