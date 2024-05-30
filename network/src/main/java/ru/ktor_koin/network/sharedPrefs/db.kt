package ru.ktor_koin.network.sharedPrefs

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

object SharedPreferencesHelper {
    const val IP_ADDRESS = "ip_address"
    private const val USER_ID = "user_id"
    private const val USER_NAME = "user_name"
    private const val IS_USER_ADMIN = "is_user_admin"
    private const val ROOM_ID = "room_id"


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

    fun getUserAdmin(): Boolean {
        return getSharedPreferences().getBoolean(IS_USER_ADMIN, false)
    }

    fun saveUserAdmin(value: Boolean) {
        getSharedPreferences().edit {
            putBoolean(IS_USER_ADMIN, value)
        }
    }

    fun getUserId(): Int {
        return getSharedPreferences().getInt(USER_ID, 0)
    }

    fun saveUserName(value: String) {
        getSharedPreferences().edit {
            putString(USER_NAME, value)
        }
    }

    fun getUserName(): String? {
        return getSharedPreferences().getString(USER_NAME, "None")
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