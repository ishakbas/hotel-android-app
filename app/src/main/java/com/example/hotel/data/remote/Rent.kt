package com.example.navbarexample.data.remote

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class Rent(
    val user_id: Int,
    val room_id: Int,
    val check_in_date: LocalDate,
    val check_out_date: LocalDate,
    val status: String?
)