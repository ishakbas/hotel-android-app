package ru.ktor_koin.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class HotelRoom(
    val id: Int = 0,
    val name: String = "",
    val roomType: String = "",
    @SerialName("room_image")
    val roomImage: ByteArray? = null
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as HotelRoom

        if (name != other.name) return false
        if (roomType != other.roomType) return false
        if (roomImage != null) {
            if (other.roomImage == null) return false
            if (!roomImage.contentEquals(other.roomImage)) return false
        } else if (other.roomImage != null) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + roomType.hashCode()
        result = 31 * result + (roomImage?.contentHashCode() ?: 0)
        return result
    }
}


@Serializable
data class HotelRoomsWithTypesExtended(
    val id: Int = 0,
    val name: String = "",
    @SerialName("room_image")
    val roomImage: ByteArray? = null,
    val roomTypeName: String = "",
    val description: String? = null,
    @SerialName("bed_count")
    val bedCount: Int? = null,
    val wifi: Boolean? = null,
    val kitchen: Boolean? = null,
    val tv: Boolean? = null
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as HotelRoomsWithTypesExtended

        if (roomImage != null) {
            if (other.roomImage == null) return false
            if (!roomImage.contentEquals(other.roomImage)) return false
        } else if (other.roomImage != null) return false

        return true
    }

    override fun hashCode(): Int {
        return roomImage?.contentHashCode() ?: 0
    }
}