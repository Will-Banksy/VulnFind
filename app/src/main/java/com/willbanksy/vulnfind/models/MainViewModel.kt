package com.willbanksy.vulnfind.models

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.paging.PagingSource
import com.willbanksy.vulnfind.data.VulnDataItem
import com.willbanksy.vulnfind.data.source.VulnRepository
import com.willbanksy.vulnfind.data.source.local.VulnDBVulnWithMetricsAndReferencesDto
import com.willbanksy.vulnfind.ui.state.ListingFilter
import kotlinx.coroutines.flow.Flow

class MainViewModel( // TODO: Make this class immutable? Or something I'm not quite sure what best practices are and how to achieve them
	private val vulnRepository: VulnRepository
) : ViewModel() {
	var apiKey by mutableStateOf("")
	
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
}