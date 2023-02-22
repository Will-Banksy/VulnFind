package com.willbanksy.vulnfind.data.source

import androidx.lifecycle.LiveData
import com.willbanksy.vulnfind.data.VulnItemState
import kotlinx.coroutines.flow.Flow

interface VulnRepository {
    suspend fun getAll(): List<VulnItemState>
    
    fun getAllLive(): LiveData<List<VulnItemState>>
    
    suspend fun insertAll(vulns: List<VulnItemState>)
}