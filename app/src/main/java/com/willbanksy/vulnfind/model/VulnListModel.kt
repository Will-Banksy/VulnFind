package com.willbanksy.vulnfind.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.willbanksy.vulnfind.data.VulnItemWithMetrics
import com.willbanksy.vulnfind.data.source.VulnRepository
import kotlinx.coroutines.flow.Flow

class VulnListModel(
	private val vulnRepository: VulnRepository
) : ViewModel() {
	var vulnsStream: Flow<List<VulnItemWithMetrics>> = vulnRepository.getVulns()
	var apiKey by mutableStateOf("")
	
//	fun refreshId(cveId: String) {
//		viewModelScope.launch { 
//			vulnRepository.refreshId(cveId)
//		}
//	}
	
	fun getById(cveId: String): Flow<VulnItemWithMetrics?> {
		return vulnRepository.getVuln(cveId)
	}
	
	// TODO: Remove?
//	fun getAllMetrics(): Flow<List<VulnMetric>> {
//		return vulnRepository.getAllMetrics()
//	}
}