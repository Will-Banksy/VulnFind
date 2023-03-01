package com.willbanksy.vulnfind.data.source.remote

import com.willbanksy.vulnfind.data.VulnItemWithMetrics
import com.willbanksy.vulnfind.data.source.VulnDataSource
import kotlinx.coroutines.flow.Flow

class VulnRemoteDataSource : VulnDataSource { // TODO: Rate Limiting
	private val client = NVDClient.getClient()
	private val api = client.create(NVDApi::class.java)
	
//    private val observableVulns = MutableStateFlow(runBlocking { getVulns() })
	
	override fun getVulnStream(cveId: String): Flow<VulnItemWithMetrics?> {
		TODO("Not yet implemented")
	}

	override fun getVulnsStream(): Flow<List<VulnItemWithMetrics>> {
		TODO("Not yet implemented")
	}

	override suspend fun getVuln(cveId: String): VulnItemWithMetrics? {
		val response = api.getCveById(cveId).execute()
		return mapToItem(response.body())
	}

	override suspend fun getVulns(): List<VulnItemWithMetrics> {
		TODO("Not yet implemented")
	}

	override suspend fun addVuln(vuln: VulnItemWithMetrics) {
		throw NotImplementedError("Operation unavailable")
	}

	override suspend fun addVulns(vulns: List<VulnItemWithMetrics>) {
		throw NotImplementedError("Operation unavailable")
	}

	override suspend fun refresh() {
		TODO("Not yet implemented")
	}
}