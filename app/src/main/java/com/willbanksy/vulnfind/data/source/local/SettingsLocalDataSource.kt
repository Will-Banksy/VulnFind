package com.willbanksy.vulnfind.data.source.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.willbanksy.vulnfind.data.SettingsData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SettingsLocalDataSource(
	private val settingsStore: DataStore<Preferences>
) {
	suspend fun update(data: SettingsData) {
		settingsStore.edit { mutPrefs ->
			mutPrefs[PKEY_API_KEY] = data.apiKey
		}
	}
	
	fun observe(): Flow<SettingsData> {
		return settingsStore.data.map { prefs ->
			SettingsData(
				apiKey = prefs[PKEY_API_KEY] ?: "",
				netOnMetered = prefs[PKEY_USE_METERED] ?: false
			)
		}
	}
	
	companion object {
		val PKEY_API_KEY = stringPreferencesKey("api_key")
		val PKEY_USE_METERED = booleanPreferencesKey("use_metered")
	}
}