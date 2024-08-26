package storage

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class SessionHandler(private val dataStore: DataStore<Preferences>) {

    companion object {
        const val DATA = "Data"
        private const val USERNAME = "Username"
        private const val TOKEN = "Token"
        private const val NAME = "Nama"
        private const val ROLE = "Role"
        private const val STORE_NAME = "Nama Toko"
        private const val ADDRESS = "Alamat"
        private const val TELP = "Telp"
        val _username = stringPreferencesKey(USERNAME)
        val _name = stringPreferencesKey(NAME)
        val _role = stringPreferencesKey(ROLE)
        val _storeName = stringPreferencesKey(STORE_NAME)
        val _address = stringPreferencesKey(ADDRESS)
        val _telp = stringPreferencesKey(TELP)
        val _userToken = stringPreferencesKey(TOKEN)
    }

    fun getUsername(): Flow<String> {
        return dataStore.data.catch {
            emit(emptyPreferences())
        }.map { pref ->
            pref[_username] ?: ""
        }
    }

    fun getToken(): Flow<String> {
        return dataStore.data.catch {
            emit(emptyPreferences())
        }.map { pref ->
            pref[_userToken] ?: ""
        }
    }

    fun getStoreName(): Flow<String> {
        return dataStore.data.catch {
            emit(emptyPreferences())
        }.map { pref ->
            pref[_storeName] ?: ""
        }
    }

    fun getAddress(): Flow<String> {
        return dataStore.data.catch {
            emit(emptyPreferences())
        }.map { pref ->
            pref[_address] ?: ""
        }
    }

    fun getTelp(): Flow<String> {
        return dataStore.data.catch {
            emit(emptyPreferences())
        }.map { pref ->
            pref[_telp] ?: ""
        }
    }

    suspend fun setUserData(
        username: String,
        nama: String,
        role: String,
        namaToko: String,
        alamat: String,
        telp: String,
        token: String
    ) {
        dataStore.edit { pref ->
            pref[_username] = username
            pref[_name] = nama
            pref[_role] = role
            pref[_storeName] = namaToko
            pref[_address] = alamat
            pref[_telp] = telp
            pref[_userToken] = token
        }
    }

    suspend fun clearData() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}