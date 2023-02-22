package com.willbanksy.vulnfind.data.source

import androidx.lifecycle.LiveData
import com.willbanksy.vulnfind.data.VulnItemState
import com.willbanksy.vulnfind.data.source.local.VulnDB
import com.willbanksy.vulnfind.data.source.local.VulnLocalDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class VulnRepository(
    private val remote: VulnLocalDataSource,
    private val local: VulnLocalDataSource
) {
    val vulns: Flow<List<VulnItemState>> = local.getVulnsStream()
    
    suspend fun refresh() {
        withContext(Dispatchers.IO) {
            // Download vulns to local database
            TODO()
        }
    }
    
    suspend fun getVuln(cveId: String): VulnItemState? {
        return local.getVuln(cveId)
    }
}