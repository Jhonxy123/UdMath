package com.example.udmath.data.repository

import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map
import android.content.Context
import kotlinx.coroutines.flow.Flow

val Context.dataStore by preferencesDataStore("settings")

class DataStoreManager(private val context: Context) {

    companion object {
        val SHOW_TOUR = booleanPreferencesKey("show_tour")
    }

    val showTour: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[SHOW_TOUR] ?: true
        }

    suspend fun setShowTour(value: Boolean) {
        context.dataStore.edit {
            it[SHOW_TOUR] = value
        }
    }
}