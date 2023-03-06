package com.willbanksy.vulnfind.data.source.local

import com.willbanksy.vulnfind.data.VulnItemWithMetrics
import com.willbanksy.vulnfind.data.VulnMetric
import com.willbanksy.vulnfind.data.source.VulnDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow

class VulnLocalDataSource(
    private val dao: VulnDBDao,
//    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : VulnDataSource {
    override fun getVulnStream(cveId: String): Flow<VulnItemWithMetrics?> {
        return dao.observeById(cveId)
    }

    override fun getVulnsStream(): Flow<List<VulnItemWithMetrics>> {
        return dao.observeAll()
    }

    override suspend fun getVuln(cveId: String): VulnItemWithMetrics? {
//        return dao.getById(cveId)
		TODO()
    }

    override suspend fun getVulns(): List<VulnItemWithMetrics> {
//        return dao.getAll()
		TODO()
    }

    override suspend fun addVuln(vuln: VulnItemWithMetrics) {
        dao.insert(vuln.item, vuln.metrics)
    }

    override suspend fun addVulns(vulns: List<VulnItemWithMetrics>) {
		for (v in vulns) {
			dao.insert(v.item, v.metrics)
		}
    }

    override suspend fun refresh() {
        // NO-OP
    }
	
	fun getAllMetrics(): Flow<List<VulnMetric>> {
		return dao.getAllMetrics()
	}
}