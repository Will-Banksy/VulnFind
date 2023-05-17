package com.willbanksy.vulnfind.data.source.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.willbanksy.vulnfind.data.SettingsData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.settingsDataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

/// Data source for locally saved settings via Preferences DataStore
class SettingsLocalDataSource(
	private val settingsStore: DataStore<Preferences>
) {
	/// Updates the saved settings data with the passed-in settings data
	suspend fun update(data: SettingsData) {
		settingsStore.edit { mutPrefs ->
				mutPrefs[PKEY_API_KEY] = data.apiKey
			mutPrefs[PKEY_USE_METERED] = data.useMetered
		}
	}
	
	// Returns an Flow for observing changes to the DataStore
	fun observe(): Flow<SettingsData> {
		val defaultSettings = SettingsData()
		return settingsStore.data.map { prefs ->
			SettingsData(
				apiKey = prefs[PKEY_API_KEY] ?: defaultSettings.apiKey,
				useMetered = prefs[PKEY_USE_METERED] ?: defaultSettings.useMetered
			)
		}
	}
	
	companion object {
		val PKEY_API_KEY = stringPreferencesKey("api_key")
		val PKEY_USE_METERED = booleanPreferencesKey("use_metered")
	}
}