package com.willbanksy.vulnfind.data.source.local

import com.willbanksy.vulnfind.data.VulnDataItem
import com.willbanksy.vulnfind.data.source.VulnDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class VulnLocalDataSource(
    private val dao: VulnDBDao,
//    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : VulnDataSource {
    fun observeById(cveId: String): Flow<VulnDataItem?> {
        return dao.observeById(cveId).map { vulnDto ->
			if(vulnDto != null) {
				mapToItem(vulnDto)
			} else {
				null
			}
		}
    }

    fun observeAll(): Flow<List<VulnDataItem>> {
        return dao.observeAll().map { vulnsDtos ->
			mapToItems(vulnsDtos)
		}
    }

    fun addVulns(vulns: List<VulnDataItem>) {
		for (v in vulns) {
			val dto = mapFromItem(v)
			dao.insert(dto.item, dto.metrics)
		}
    }
	
//	fun getAllMetrics(): Flow<List<VulnMetric>> {
//		return dao.getAllMetrics()
//	}
}