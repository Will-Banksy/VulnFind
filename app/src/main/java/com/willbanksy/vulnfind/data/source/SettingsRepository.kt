package com.willbanksy.vulnfind.data.source

import com.willbanksy.vulnfind.data.SettingsData
import com.willbanksy.vulnfind.data.source.local.SettingsLocalDataSource
import kotlinx.coroutines.flow.Flow

class SettingsRepository(
	val local: SettingsLocalDataSource
) {
	suspend fun update(data: SettingsData) {
		local.update(data)
	}
	
	fun observe(): Flow<SettingsData> {
		return local.observe()
	}
}