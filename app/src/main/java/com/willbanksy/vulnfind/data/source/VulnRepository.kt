package com.willbanksy.vulnfind.data.source

import com.willbanksy.vulnfind.data.VulnItemWithMetrics
import com.willbanksy.vulnfind.data.VulnMetric
import com.willbanksy.vulnfind.data.source.local.VulnLocalDataSource
import com.willbanksy.vulnfind.data.source.remote.VulnRemoteDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class VulnRepository(
	private val remote: VulnRemoteDataSource,
	private val local: VulnLocalDataSource
) {
	val vulns: Flow<List<VulnItemWithMetrics>> = local.getVulnsStream()
	
	data class PagingInfo(
		val itemsPerPage: Int,
		val totalItems: Int
	)
	
	suspend fun refreshWithPagingInfo(): PagingInfo? {
		var pagingInfo: PagingInfo?
		withContext(Dispatchers.IO) {
			// Download vulns to local database
			val dataWithPagingInfo = remote.refreshWithPagingInfo()
			pagingInfo = dataWithPagingInfo?.pagingInfo
			local.addVulns(dataWithPagingInfo?.data ?: emptyList())
		}
		return pagingInfo
	}
	
	suspend fun refreshSection(sectionIdx: Int, info: PagingInfo) {
		withContext(Dispatchers.IO) {
			val vulns = remote.refreshSection(sectionIdx, info)
			local.addVulns(vulns)
		}
	}
	
	suspend fun refreshId(cveId: String) {
		withContext(Dispatchers.IO) {
			val remoteCve = remote.getVuln(cveId)
			if(remoteCve != null) { 
				local.addVuln(remoteCve)
			}
		}
	}
	
	fun getAllMetrics(): Flow<List<VulnMetric>> {
		return local.getAllMetrics()
	}
	
	fun getVulnStream(cveId: String): Flow<VulnItemWithMetrics?> {
		// BUG: This will return null if the CVE ID is not in the local database. Need some way of fetching from remote
		return local.getVulnStream(cveId)
	}

	suspend fun getVuln(cveId: String): VulnItemWithMetrics? {
		var localVuln = local.getVuln(cveId)
		if(localVuln == null) {
			refreshId(cveId)
			localVuln = local.getVuln(cveId)
		}
		return localVuln
	}
	
	suspend fun getVulns(): List<VulnItemWithMetrics> {
		return local.getVulns()
	}
	
	suspend fun manuallySaveToDB(vulns: List<VulnItemWithMetrics>) {
		withContext(Dispatchers.IO) {
			local.addVulns(vulns)
		}
	}
}