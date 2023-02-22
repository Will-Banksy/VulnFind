package com.willbanksy.vulnfind.data.source

import com.willbanksy.vulnfind.data.VulnItemState
import kotlinx.coroutines.flow.Flow

interface VulnDataSource {
    fun getVulnStream(cveId: String): Flow<VulnItemState?>
    
    fun getVulnsStream(): Flow<List<VulnItemState>>
    
    suspend fun getVuln(cveId: String): VulnItemState?
    
    suspend fun getVulns(): List<VulnItemState>
    
    suspend fun addVuln(vuln: VulnItemState)
    
    suspend fun addVulns(vulns: List<VulnItemState>)
    
    suspend fun refresh()
}