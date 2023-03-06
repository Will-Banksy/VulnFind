package com.willbanksy.vulnfind.data.source.remote

import com.willbanksy.vulnfind.data.VulnItemWithMetrics
import com.willbanksy.vulnfind.data.source.VulnDataSource
import com.willbanksy.vulnfind.data.source.VulnRepository
import kotlinx.coroutines.flow.Flow

class VulnRemoteDataSource : VulnDataSource { // TODO: Rate Limiting
	data class DataWithPagingInfo(
		val pagingInfo: VulnRepository.PagingInfo,
		val data: List<VulnItemWithMetrics>
	)
	
	private val client = NVDClient.getClient()
	private val api = client.create(NVDApi::class.java)
	
	override fun getVulnStream(cveId: String): Flow<VulnItemWithMetrics?> {
		throw UnsupportedOperationException()
	}

	override fun getVulnsStream(): Flow<List<VulnItemWithMetrics>> {
		throw UnsupportedOperationException()
	}

	override suspend fun getVuln(cveId: String): VulnItemWithMetrics? {
		val response = api.getCveById(cveId).execute()
		return mapToItems(response.body()).getOrNull(0)
	}

	override suspend fun getVulns(): List<VulnItemWithMetrics> {
		TODO("Not yet implemented")
	}

	override suspend fun addVuln(vuln: VulnItemWithMetrics) {
		throw UnsupportedOperationException()
	}

	override suspend fun addVulns(vulns: List<VulnItemWithMetrics>) {
		throw UnsupportedOperationException()
	}

	override suspend fun refresh() {
		TODO("Not yet implemented")
	}

	fun refreshWithPagingInfo(): DataWithPagingInfo? {
		val listing = api.getInitial().execute().body() ?: return null
		val pagingInfo = VulnRepository.PagingInfo(itemsPerPage = listing.resultsPerPage, totalItems = listing.totalResults)
		return DataWithPagingInfo(pagingInfo = pagingInfo, data = mapToItems(listing))
	}
	
	fun refreshSection(sectionIdx: Int, info: VulnRepository.PagingInfo): List<VulnItemWithMetrics> {
		val listing = api.getSection(info.itemsPerPage * sectionIdx).execute().body()
		return mapToItems(listing)
	}
}