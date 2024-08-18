package storage

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class SessionHandler (private val dataStore: DataStore<Preferences>) {

    companion object {
        const val DATA = "Data"
        private const val UserId = "UserId"
        private const val USERNAME = "Username"
        private const val TOKEN = "Token"
        val name = stringPreferencesKey(USERNAME)
        val userToken = stringPreferencesKey(TOKEN)
        val userId = stringPreferencesKey(UserId)
    }

    fun getUsername(): Flow<String> {
        return dataStore.data.catch {
            emit(emptyPreferences())
        }.map { pref ->
            pref[name] ?: ""
        }
    }

    fun getToken(): Flow<String> {
        return dataStore.data.catch {
            emit(emptyPreferences())
        }.map { pref ->
            pref[userToken] ?: ""
        }
    }

    fun getUserId(): Flow<String> {
        return dataStore.data.catch {
            emit(emptyPreferences())
        }.map { pref ->
            pref[userId] ?: ""
        }
    }

    suspend fun setUserToken(token: String) {
        dataStore.edit { pref ->
            pref[userToken] = token
        }
    }

    suspend fun setUserData(username: String, token: String, idUser: String) {
        dataStore.edit { pref ->
            pref[name] = username
            pref[userToken] = token
            pref[userId] = idUser
        }
    }

    suspend fun clearData() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}