package com.example.hotel.data.remote

import kotlinx.serialization.Serializable

@Serializable
data class HotelRoom(
    val id: Int,
    val name: String,
    val roomType: String,
    val room_image: ByteArray?
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as HotelRoom

        if (name != other.name) return false
        if (roomType != other.roomType) return false
        if (room_image != null) {
            if (other.room_image == null) return false
            if (!room_image.contentEquals(other.room_image)) return false
        } else if (other.room_image != null) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + roomType.hashCode()
        result = 31 * result + (room_image?.contentHashCode() ?: 0)
        return result
    }
}