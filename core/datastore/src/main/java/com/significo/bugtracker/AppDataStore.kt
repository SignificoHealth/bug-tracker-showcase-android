package com.significo.bugtracker

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class AppDataStore(context: Context) {
    private val dataStore = context.dataStore

    val user: Flow<String?> = dataStore.data.map { preferences ->
        preferences[USER]
    }

    suspend fun clearUser() {
        dataStore.edit { preferences ->
            preferences.remove(USER)
        }
    }

    suspend fun updateUser(user: String) {
        dataStore.edit { preferences ->
            preferences[USER] = user
        }
    }

    internal companion object {
        val USER = stringPreferencesKey(name = "user")
    }
}
