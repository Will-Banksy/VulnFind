package com.willbanksy.vulnfind.data.source.local

import com.willbanksy.vulnfind.data.VulnDataItem
import com.willbanksy.vulnfind.data.VulnDataItemMetric
import com.willbanksy.vulnfind.data.VulnDataItemReference
import com.willbanksy.vulnfind.utils.pickPrimaryMetricId
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

fun mapToItem(itemDto: VulnDBVulnWithMetricsAndReferencesDto): VulnDataItem {
	return VulnDataItem(
		cveId = itemDto.item.cveId,
		description = itemDto.item.description,
		publishedDate = itemDto.item.publishedDate,
		lastModifiedDate = itemDto.item.lastModifiedDate,
		sourceId = itemDto.item.sourceId,
		bookmarked = itemDto.item.bookmarked,
		metrics = itemDto.metrics.map { metricDto ->
			VulnDataItemMetric(
				version = metricDto.version,
				vectorString = metricDto.vectorString,
				baseScore = metricDto.baseScore,
				baseSeverity = metricDto.baseSeverity
			)
		},
		references = itemDto.references.map { refDto ->
			VulnDataItemReference(
				url = refDto.url,
				source = refDto.source,
				tags = refDto.tags.split(";")
			)
		}
	)
}

fun mapFromItem(item: VulnDataItem): VulnDBVulnWithMetricsAndReferencesDto {
	val primaryMetricId = pickPrimaryMetricId(item.metrics)
	return VulnDBVulnWithMetricsAndReferencesDto(
		item = VulnDBVulnDto(
			cveId = item.cveId,
			description = item.description,
			publishedDate = item.publishedDate,
			lastModifiedDate = item.lastModifiedDate,
			publishedDateUnix = LocalDateTime.parse(item.publishedDate, DateTimeFormatter.ISO_DATE_TIME).toEpochSecond(
				ZoneOffset.UTC),
			sourceId = item.sourceId,
			bookmarked = item.bookmarked,
			primaryMetric = mapFromMetricItem(item.metrics.getOrNull(primaryMetricId), item.cveId, primaryMetricId)
		),
		metrics = item.metrics.mapIndexed { index, metric ->
			mapFromMetricItem(metric, item.cveId, index)
		},
		references = item.references.map { ref ->
			VulnDBReferenceDto(
				ofCveId = item.cveId,
				url = ref.url,
				source = ref.source,
				tags = ref.tags.foldIndexed("") { i, tag, acc ->
					if(i == 0) {
						acc.plus(tag)
					} else {
						acc.plus(";${tag}")
					}
				}
			)
		}
	)
}

fun mapFromMetricItem(metric: VulnDataItemMetric?, cveId: String, index: Int): VulnDBMetricDto {
	return VulnDBMetricDto(
		id = "${cveId}.${index}",
		ofCveId = cveId,
		version = metric?.version ?: "N",
		vectorString = metric?.vectorString ?: "",
		baseScore = metric?.baseScore ?: 0f,
		baseSeverity = metric?.baseSeverity ?: ""
	)
}