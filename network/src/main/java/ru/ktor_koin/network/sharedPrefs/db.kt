package ru.ktor_koin.network.sharedPrefs

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

object SharedPreferencesHelper {
    const val IP_ADDRESS = "ip_address"
    const val USER_ID = "user_id"
    const val ROOM_ID = "room_id"

    private lateinit var sharedPref: SharedPreferences

    fun init(preferenceName: String, context: Context) {
        when (preferenceName) {
            IP_ADDRESS -> sharedPref =
                context.getSharedPreferences(IP_ADDRESS, Context.MODE_PRIVATE)

            USER_ID -> sharedPref = context.getSharedPreferences(USER_ID, Context.MODE_PRIVATE)
        }
    }

    private fun getSharedPreferences(): SharedPreferences {
        return sharedPref
    }

    fun getIpAddress(): String {
        return getSharedPreferences().getString(IP_ADDRESS, "Ip-адрес не указан").toString()
    }

    fun saveIpAddress(value: String) {
        getSharedPreferences().edit {
            putString(IP_ADDRESS, value)
        }
    }

    fun saveUserId(value: Int) {
        getSharedPreferences().edit {
            putInt(USER_ID, value)
        }
    }

    fun getUserId(): Int {
        return getSharedPreferences().getInt(USER_ID, 0)
    }

    fun saveRoomId(value: Int) {
        getSharedPreferences().edit {
            putInt(ROOM_ID, value)
        }
    }

    fun getRoomId(): Int {
        return getSharedPreferences().getInt(ROOM_ID, 0)
    }
}