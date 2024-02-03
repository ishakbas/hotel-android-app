package com.example.hotel.data.store

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.hotel.data.remote.UserInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class DataStoreManager(private val context: Context) {

    companion object {
        private val Context.datastore: DataStore<Preferences> by preferencesDataStore("IpAddress")
        val IP_ADDRESS_KEY = stringPreferencesKey("ip_address")

        private val Context.userDatastore: DataStore<Preferences> by preferencesDataStore("User")
        val USER_ID_KEY = intPreferencesKey("user_id")
        val USER_LOGIN_KEY = stringPreferencesKey("user_login")
        val USER_PASSWORD_KEY = stringPreferencesKey("user_password")
        val USER_ADMIN_KEY = booleanPreferencesKey("user_isAdmin")
    }

    val getIpAddress: Flow<String> =
        context.datastore.data.map { pref ->
            pref[IP_ADDRESS_KEY] ?: ""
        }

    suspend fun saveIpAddress(ipAddress: String) {
        context.datastore.edit { pref ->
            pref[IP_ADDRESS_KEY] = ipAddress
        }
    }

    fun getUserInfo() =
        context.userDatastore.data.map { pref ->
            return@map UserInfo(
                pref[USER_ID_KEY] ?: 0,
                pref[USER_LOGIN_KEY] ?: "",
                pref[USER_PASSWORD_KEY] ?: "",
                pref[USER_ADMIN_KEY] ?: false
            )
        }

    suspend fun saveUserInfo(user: UserInfo) {
        context.userDatastore.edit { pref ->
            pref[USER_ID_KEY] = user.id
            pref[USER_LOGIN_KEY] = user.login
            pref[USER_PASSWORD_KEY] = user.password
            pref[USER_ADMIN_KEY] = user.admin
        }
    }
}