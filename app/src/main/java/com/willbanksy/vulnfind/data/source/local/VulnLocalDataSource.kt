package com.willbanksy.vulnfind.data.source.local

import android.util.Log
import androidx.paging.PagingSource
import com.willbanksy.vulnfind.data.VulnDataItem
import com.willbanksy.vulnfind.data.source.VulnDataSource
import com.willbanksy.vulnfind.ui.state.ListingFilter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.io.DataInput
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime

class VulnLocalDataSource(
    private val dao: VulnDBDao,
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

    fun observeAll(filter: ListingFilter? = null): PagingSource<Int, VulnDBVulnWithMetricsDto> {
		if(filter == null) {
			return dao.observeAll()
		}
		
		val month: Int = filter.month?.value ?: 1
		val year: Int = filter.year?.value ?: 2000
		val dateCmpFormat = if(filter.month != null && filter.year != null) {
			"%m%Y"
		} else if(filter.year != null) {
			"%Y"
		} else if(filter.month != null) {
			"%m"
		} else {
			""
		}
		
		return dao.observeAllFiltered(
			ZonedDateTime.of(
				year, month, 1, 0, 0, 0, 0,
				ZoneId.ofOffset("UTC", ZoneOffset.UTC)
			).toEpochSecond(),
			dateCmpFormat
		)
		
		// TODO: Find a way to map the contents of this PagingSource to VulnDataItem
//			.map<Int, VulnDBVulnWithMetricsDto> {
//			mapToItems(vulnsDtos)
//		}
    }

    fun addVulns(vulns: List<VulnDataItem>) {
		val vulnsDtos: MutableList<VulnDBVulnDto> = mutableListOf()
		val metricsDtos: MutableList<VulnDBMetricDto> = mutableListOf()
		
		for(item in vulns) {
			val dto = mapFromItem(item)
			vulnsDtos.add(dto.item)
			metricsDtos.addAll(dto.metrics)
		}
		
		dao.insertAllVulns(vulnsDtos)
		dao.insertAllMetrics(metricsDtos)
    }
	
//	fun getAllMetrics(): Flow<List<VulnMetric>> {
//		return dao.getAllMetrics()
//	}
}