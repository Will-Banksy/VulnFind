package com.willbanksy.vulnfind.data.source

import com.willbanksy.vulnfind.data.VulnItemWithMetrics
import kotlinx.coroutines.flow.Flow

interface VulnDataSource {
    fun getVulnStream(cveId: String): Flow<VulnItemWithMetrics?>
    
    fun getVulnsStream(): Flow<List<VulnItemWithMetrics>>
    
    suspend fun getVuln(cveId: String): VulnItemWithMetrics?
    
    suspend fun getVulns(): List<VulnItemWithMetrics>
    
    suspend fun addVuln(vuln: VulnItemWithMetrics)
    
    suspend fun addVulns(vulns: List<VulnItemWithMetrics>)
    
    suspend fun refresh()
}