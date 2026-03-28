package cv.data.repository

import android.content.SharedPreferences
import androidx.core.content.edit
import cv.domain.repositories.LocalStorageRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class LocalStorageRepositoryImp(
    private val preferences: SharedPreferences,
) : LocalStorageRepository {
    override fun setString(
        key: String,
        value: String,
    ) = preferences.edit { putString(key, value) }

    override fun getString(key: String): String = preferences.getString(key, "") ?: ""

    override fun onStorageChanged(): Flow<Unit> =
        callbackFlow {
            val listener =
                SharedPreferences.OnSharedPreferenceChangeListener { _, _ ->
                    trySend(Unit)
                }
            preferences.registerOnSharedPreferenceChangeListener(listener)
            awaitClose { preferences.unregisterOnSharedPreferenceChangeListener(listener) }
        }
}
