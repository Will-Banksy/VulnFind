package com.willbanksy.vulnfind.data.source.local

import com.willbanksy.vulnfind.data.VulnDataItem
import com.willbanksy.vulnfind.data.VulnDataItemMetric
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

fun mapToItems(listingDto: List<VulnDBVulnWithMetricsDto>): List<VulnDataItem> {
	return listingDto.map { itemDto ->
		mapToItem(itemDto)
	}
}

fun mapToItem(itemDto: VulnDBVulnWithMetricsDto): VulnDataItem {
	return VulnDataItem(
		cveId = itemDto.item.cveId,
		description = itemDto.item.description,
		publishedDate = itemDto.item.publishedDate,
		lastModifiedDate = itemDto.item.lastModifiedDate,
		metrics = itemDto.metrics.map { metricDto ->
			VulnDataItemMetric(
				version = metricDto.version,
				vectorString = metricDto.vectorString,
				baseScore = metricDto.baseScore,
				baseSeverity = metricDto.baseSeverity
			)
		}
	)
}

fun mapFromItems(items: List<VulnDataItem>): List<VulnDBVulnWithMetricsDto> {
	return items.map { item -> 
		mapFromItem(item)
	}
}

fun mapFromItem(item: VulnDataItem): VulnDBVulnWithMetricsDto {
	return VulnDBVulnWithMetricsDto(
		item = VulnDBVulnDto(
			cveId = item.cveId,
			description = item.description,
			publishedDate = item.publishedDate,
			lastModifiedDate = item.lastModifiedDate,
			publishedDateUnix = LocalDateTime.parse(item.publishedDate, DateTimeFormatter.ISO_DATE_TIME).toEpochSecond(
				ZoneOffset.UTC)
		),
		metrics = item.metrics.mapIndexed { index, metric ->
			VulnDBMetricDto(
				id = "${item.cveId}.${index}",
				ofCveId = item.cveId,
				version = metric.version,
				vectorString = metric.vectorString,
				baseScore = metric.baseScore,
				baseSeverity = metric.baseSeverity
			)
		}
	)
}