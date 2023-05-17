package com.willbanksy.vulnfind.models

import androidx.lifecycle.ViewModel
import androidx.paging.PagingSource
import com.willbanksy.vulnfind.data.SettingsData
import com.willbanksy.vulnfind.data.VulnDataItem
import com.willbanksy.vulnfind.data.source.SettingsRepository
import com.willbanksy.vulnfind.data.source.VulnRepository
import com.willbanksy.vulnfind.data.source.local.VulnDBVulnWithMetricsAndReferencesDto
import com.willbanksy.vulnfind.ui.state.ListingFilter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

class MainViewModel(
	private val vulnRepository: VulnRepository,
	private val settingsRepository: SettingsRepository
) : ViewModel() {
	private var settingsData: SettingsData? = null
	
	fun observeById(cveId: String): Flow<VulnDataItem?> {
		return vulnRepository.observeById(cveId)
	}
	
	fun observeAll(filter: ListingFilter? = null): PagingSource<Int, VulnDBVulnWithMetricsAndReferencesDto> {
		return vulnRepository.observeAll(filter)
	}
	
	suspend fun setBookmarked(item: VulnDataItem, bookmarked: Boolean) {
		vulnRepository.setBookmarked(item, bookmarked)
	}
	
	fun observeBookmarked(): PagingSource<Int, VulnDBVulnWithMetricsAndReferencesDto> {
		return vulnRepository.observeBookmarked()
	}
	
	fun observeAllFilteredById(cveId: String): PagingSource<Int, VulnDBVulnWithMetricsAndReferencesDto> {
		return vulnRepository.observeAllFilteredById(cveId)
	}
	
	suspend fun updateSettingApiKey(value: String) {
		val latest = settingsData ?: observeSettings().firstOrNull() ?: SettingsData()
		if(settingsData == null) {
			settingsData = latest
		}
		settingsRepository.update(latest.copy(apiKey = value))
	}
	
	suspend fun updateSettingUseMetered(value: Boolean) {
		val latest = settingsData ?: observeSettings().firstOrNull() ?: SettingsData()
		if(settingsData == null) {
			settingsData = latest
		}
		settingsRepository.update(latest.copy(useMetered = value))
	}
	
	fun observeSettings(): Flow<SettingsData> {
		return settingsRepository.observe()
	}
}