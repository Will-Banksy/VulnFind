package com.willbanksy.vulnfind.data.source

import androidx.paging.PagingSource
import com.willbanksy.vulnfind.data.VulnDataItem
import com.willbanksy.vulnfind.data.source.local.VulnDBVulnWithMetricsAndReferencesDto
import com.willbanksy.vulnfind.data.source.local.VulnLocalDataSource
import com.willbanksy.vulnfind.data.source.remote.VulnRemoteDataSource
import com.willbanksy.vulnfind.ui.state.ListingFilter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class VulnRepository(
	private val remote: VulnRemoteDataSource,
	private val local: VulnLocalDataSource
) {
	data class PagingInfo(
		val itemsPerPage: Int,
		val totalItems: Int
	)
	
	suspend fun refreshWithPagingInfo(apiKey: String?): PagingInfo? {
		var pagingInfo: PagingInfo?
		withContext(Dispatchers.IO) {
			// Download vulns to local database
			val dataWithPagingInfo = remote.refreshWithPagingInfo(apiKey)
			pagingInfo = dataWithPagingInfo?.pagingInfo
			local.addVulns(dataWithPagingInfo?.data ?: emptyList())
		}
		return pagingInfo
	}
	
	suspend fun refreshSection(sectionIdx: Int, info: PagingInfo, apiKey: String?) {
		withContext(Dispatchers.IO) {
			val vulns = remote.refreshSection(sectionIdx, info, apiKey)
			local.addVulns(vulns)
		}
	}
	
	fun observeById(cveId: String): Flow<VulnDataItem?> {
		// BUG: This will return null if the CVE ID is not in the local database. Need some way of fetching from remote
		return local.observeById(cveId)
	}
	
	fun observeAll(filter: ListingFilter? = null): PagingSource<Int, VulnDBVulnWithMetricsAndReferencesDto> {
		return local.observeAll(filter)
	}
	
	suspend fun setBookmarked(item: VulnDataItem, bookmarked: Boolean) {
		withContext(Dispatchers.IO) {
			local.setBookmarked(item, bookmarked)
		}
	}
	
	fun observeBookmarked(): PagingSource<Int, VulnDBVulnWithMetricsAndReferencesDto> {
		return local.observeBookmarked()
	}
	
	fun observeAllFilteredById(cveId: String): PagingSource<Int, VulnDBVulnWithMetricsAndReferencesDto> {
		return local.observeAllFilteredById(cveId)
	}
}