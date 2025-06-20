package dev.mbakasir.com.storage

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
        val _username = stringPreferencesKey("Username")
        val _name = stringPreferencesKey("Nama")
        val _role = stringPreferencesKey("Role")
        val _storeName = stringPreferencesKey("Nama Toko")
        val _address = stringPreferencesKey("Alamat")
        val _telp = stringPreferencesKey("Telp")
        val _userToken = stringPreferencesKey("Token")
        val _lastUpdate = stringPreferencesKey("Last Update")
    }

    private fun <T> getPreference(key: Preferences.Key<T>, defaultValue: T): Flow<T> {
        return dataStore.data.catch {
            emit(emptyPreferences())
        }.map { preferences ->
            preferences[key] ?: defaultValue
        }
    }

    fun getUsername(): Flow<String> = getPreference(_username, "")
    fun getRole(): Flow<String> = getPreference(_role, "")
    fun getName(): Flow<String> = getPreference(_name, "")
    fun getToken(): Flow<String> = getPreference(_userToken, "")
    fun getStoreName(): Flow<String> = getPreference(_storeName, "")
    fun getAddress(): Flow<String> = getPreference(_address, "")
    fun getTelp(): Flow<String> = getPreference(_telp, "")
    fun getLastUpdate(): Flow<String> = getPreference(_lastUpdate, "")

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
            val lastUpdateValue = preferences[_lastUpdate]
            preferences.clear()
            if (lastUpdateValue != null) {
                preferences[_lastUpdate] = lastUpdateValue
            }
        }
    }

    suspend fun setLastUpdate(lastUpdate: String) {
        dataStore.edit { pref ->
            pref[_lastUpdate] = lastUpdate
        }
    }
}