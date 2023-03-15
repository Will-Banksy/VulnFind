package com.willbanksy.vulnfind.data.source.local

import androidx.paging.PagingSource
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

    fun observeAll(): PagingSource<Int, VulnDBVulnWithMetricsDto> {
        return dao.observeAll() // TODO: Find a way to map the contents of this PagingSource to VulnDataItem
//			.map<Int, VulnDBVulnWithMetricsDto> {
//			mapToItems(vulnsDtos)
//		}
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