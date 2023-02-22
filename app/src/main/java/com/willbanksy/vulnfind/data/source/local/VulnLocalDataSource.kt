package com.willbanksy.vulnfind.data.source.local

import com.willbanksy.vulnfind.data.VulnItemState
import com.willbanksy.vulnfind.data.source.VulnDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow

class VulnLocalDataSource(
    private val dao: VulnDBDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : VulnDataSource {
    override fun getVulnStream(cveId: String): Flow<VulnItemState?> {
        return dao.observeById(cveId)
    }

    override fun getVulnsStream(): Flow<List<VulnItemState>> {
        return dao.observeAll()
    }

    override suspend fun getVuln(cveId: String): VulnItemState? {
        return dao.getById(cveId)
    }

    override suspend fun getVulns(): List<VulnItemState> {
        return dao.getAll()
    }

    override suspend fun addVuln(vuln: VulnItemState) {
        dao.insert(vuln)
    }

    override suspend fun addVulns(vulns: List<VulnItemState>) {
        dao.insertAll(vulns)
    }

    override suspend fun refresh() {
        // NO-OP
    }
}