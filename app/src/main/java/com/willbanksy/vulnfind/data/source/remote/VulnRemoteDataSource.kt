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

	fun refreshWithPagingInfo(apiKey: String?): DataWithPagingInfo? {
		val listing = if(!apiKey.isNullOrEmpty()) {
			api.getInitialWithKey(apiKey)
		} else {
			api.getInitial()
		}.execute().body() ?: return null
		val pagingInfo = VulnRepository.PagingInfo(itemsPerPage = listing.resultsPerPage, totalItems = listing.totalResults)
		return DataWithPagingInfo(pagingInfo = pagingInfo, data = mapToItems(listing))
	}
	
	fun refreshSection(sectionIdx: Int, info: VulnRepository.PagingInfo, apiKey: String?): List<VulnDataItem> {
		val listing = if(!apiKey.isNullOrEmpty()) {
			api.getSectionWithKey(apiKey, info.itemsPerPage * sectionIdx)
		} else {
			api.getSection(info.itemsPerPage * sectionIdx)
		}.execute().body()
		return mapToItems(listing)
	}
}