package com.willbanksy.vulnfind.data.source.remote

import com.willbanksy.vulnfind.data.VulnDataItem
import com.willbanksy.vulnfind.data.source.VulnDataSource
import com.willbanksy.vulnfind.data.source.VulnRepository

class VulnRemoteDataSource : VulnDataSource { // TODO: Rate Limiting
	data class DataWithPagingInfo(
		val pagingInfo: VulnRepository.PagingInfo,
		val data: List<VulnDataItem>
	)
	
	private val client = NvdClient.getClient()
	private val api = client.create(NvdApi::class.java)

//	fun getVuln(cveId: String): VulnItemWithMetrics? {
//		val response = api.getCveById(cveId).execute()
//		return mapToItems(response.body()).getOrNull(0)
//	}

	fun refreshWithPagingInfo(): DataWithPagingInfo? {
		val listing = api.getInitial().execute().body() ?: return null
		val pagingInfo = VulnRepository.PagingInfo(itemsPerPage = listing.resultsPerPage, totalItems = listing.totalResults)
		return DataWithPagingInfo(pagingInfo = pagingInfo, data = mapToItems(listing))
	}
	
	fun refreshSection(sectionIdx: Int, info: VulnRepository.PagingInfo): List<VulnDataItem> {
		val listing = api.getSection(info.itemsPerPage * sectionIdx).execute().body()
		return mapToItems(listing)
	}
}