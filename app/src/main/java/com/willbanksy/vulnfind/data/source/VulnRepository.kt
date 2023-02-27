package com.willbanksy.vulnfind.data.source

import com.willbanksy.vulnfind.data.VulnItemState
import com.willbanksy.vulnfind.data.source.local.VulnLocalDataSource
import com.willbanksy.vulnfind.data.source.remote.VulnRemoteDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class VulnRepository(
    private val remote: VulnRemoteDataSource,
    private val local: VulnLocalDataSource
) {
    val vulns: Flow<List<VulnItemState>> = local.getVulnsStream()
    
    suspend fun refresh() {
        withContext(Dispatchers.IO) {
            // Download vulns to local database
            TODO("Refreshing/downloading of DB from NVD not yet implemented")
        }
    }
    
    suspend fun getVuln(cveId: String): VulnItemState? {
        return local.getVuln(cveId)
    }
    
    suspend fun getVulns(): List<VulnItemState> {
        return local.getVulns()
    }
    
    suspend fun manuallySaveToDB(vulns: List<VulnItemState>) {
        withContext(Dispatchers.IO) {
            local.addVulns(vulns)
        }
    }
}