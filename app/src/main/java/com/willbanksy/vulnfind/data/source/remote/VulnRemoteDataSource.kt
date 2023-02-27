package com.willbanksy.vulnfind.data.source.remote

import com.willbanksy.vulnfind.data.VulnItemState
import com.willbanksy.vulnfind.data.source.VulnDataSource
import kotlinx.coroutines.flow.Flow

class VulnRemoteDataSource : VulnDataSource { // TODO: Use Retrofit for this
	private val client = NVDClient.getClient()
	private val api = client.create(NVDApi::class.java)
	
//    private val observableVulns = MutableStateFlow(runBlocking { getVulns() })
	
	override fun getVulnStream(cveId: String): Flow<VulnItemState?> {
		TODO("Not yet implemented")
	}

	override fun getVulnsStream(): Flow<List<VulnItemState>> {
		TODO("Not yet implemented")
	}

	override suspend fun getVuln(cveId: String): VulnItemState? {
		val response = api.getCveById(cveId).execute()
		return mapToItem(response.body())
	}

	override suspend fun getVulns(): List<VulnItemState> {
		TODO("Not yet implemented")
	}

	override suspend fun addVuln(vuln: VulnItemState) {
		throw NotImplementedError("Operation unavailable")
	}

	override suspend fun addVulns(vulns: List<VulnItemState>) {
		throw NotImplementedError("Operation unavailable")
	}

	override suspend fun refresh() {
		TODO("Not yet implemented")
	}
}