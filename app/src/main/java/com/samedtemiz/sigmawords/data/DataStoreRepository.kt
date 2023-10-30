package com.samedtemiz.sigmawords.data

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "on_boarding_pref")
val Context.quizDataStore: DataStore<Preferences> by preferencesDataStore(name = "quiz_state_pref")

class DataStoreRepository(context: Context) {

    private object PreferencesKey {
        val onBoardingKey = booleanPreferencesKey(name = "on_boarding_completed")
        val quizStateKey = booleanPreferencesKey(name="quiz_state_solved")
    }

    private val dataStore = context.dataStore
    private val quizDataStore = context.quizDataStore

    suspend fun resetQuizState() {
        quizDataStore.edit { preferences ->
            preferences[PreferencesKey.quizStateKey] = false
        }
    }

    suspend fun saveQuizState(solved: Boolean) {
        quizDataStore.edit { preferences ->
            preferences[PreferencesKey.quizStateKey] = solved
        }

        Log.d("DataStore", "Quiz state saved: $solved")
    }

    fun readQuizState(): Flow<Boolean> {
        return quizDataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                val quizState = preferences[PreferencesKey.quizStateKey] ?: false
                quizState
            }
    }

    // On Board
    suspend fun saveOnBoardingState(completed: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKey.onBoardingKey] = completed
        }

        Log.d("DataStore", "Onboarding state saved: $completed")
    }

    fun readOnBoardingState(): Flow<Boolean> {
        return dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                val onBoardingState = preferences[PreferencesKey.onBoardingKey] ?: false
                onBoardingState
            }
    }
}