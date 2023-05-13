package com.willbanksy.vulnfind.data.source.local

import androidx.paging.PagingSource
import com.willbanksy.vulnfind.data.VulnDataItem
import com.willbanksy.vulnfind.data.source.VulnDataSource
import com.willbanksy.vulnfind.ui.state.ListingFilter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
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

    fun observeAll(filter: ListingFilter? = null): PagingSource<Int, VulnDBVulnWithMetricsAndReferencesDto> {
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
		
		return dao.observeAllFilteredMetrics(
			ZonedDateTime.of(
				year, month, 1, 0, 0, 0, 0,
				ZoneId.ofOffset("UTC", ZoneOffset.UTC)
			).toEpochSecond(),
			dateCmpFormat,
			filter.minScore,
			filter.maxScore,
			filter.text.let { if(filter.text.isEmpty() || filter.text.isBlank()) "%" else "%${filter.text}%" },
			if(filter.showEmptyMetrics) "A" else "N"
		)
		
		// TODO: Find a way to map the contents of this PagingSource to VulnDataItem
    }

    fun addVulns(vulns: List<VulnDataItem>) {
		val vulnsDtos: MutableList<VulnDBVulnDto> = mutableListOf()
		val metricsDtos: MutableList<VulnDBMetricDto> = mutableListOf()
		val refsDtos: MutableList<VulnDBReferenceDto> = mutableListOf()
		
		for(item in vulns) {
			val dto = mapFromItem(item)
			vulnsDtos.add(dto.item)
			metricsDtos.addAll(dto.metrics)
			refsDtos.addAll(dto.references)
		}
		
		dao.insertAllVulns(vulnsDtos)
		dao.insertAllMetrics(metricsDtos)
		dao.insertAllReferences(refsDtos)
    }
	
	fun setBookmarked(item: VulnDataItem, bookmarked: Boolean) {
		val itemDto = mapFromItem(item)
		
		val vulnDto = itemDto.item.copy(bookmarked = bookmarked)
		
		dao.updateVuln(vulnDto)
	}
	
	fun observeBookmarked(): PagingSource<Int, VulnDBVulnWithMetricsAndReferencesDto> {
		return dao.observeBookmarked()
	}
}