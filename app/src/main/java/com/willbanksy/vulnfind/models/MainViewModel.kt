package com.willbanksy.vulnfind.models

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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
	
	fun getById(cveId: String): Flow<VulnDataItem?> {
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
	
	suspend fun updateApiKey(string: String) {
		val latest = settingsData ?: observeSettings().firstOrNull() ?: SettingsData("", false)
		if(settingsData == null) {
			settingsData = latest
		}
		settingsRepository.update(latest.copy(apiKey = string))
	}
	
	fun observeSettings(): Flow<SettingsData> {
		return settingsRepository.observe()
	}
}