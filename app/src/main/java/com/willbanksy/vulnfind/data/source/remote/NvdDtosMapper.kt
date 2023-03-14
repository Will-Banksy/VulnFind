package com.willbanksy.vulnfind.data.source.remote

import com.willbanksy.vulnfind.data.VulnItem
import com.willbanksy.vulnfind.data.VulnItemWithMetrics
import com.willbanksy.vulnfind.data.VulnMetric
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

fun mapToItems(listingDto: CveListingDto?): List<VulnItemWithMetrics> {
	return listingDto.let { cveListing ->
		if(cveListing == null) {
			return@let emptyList()
		}
		return@let cveListing.vulnerabilities.map { vuln ->
			val cve = vuln.cve
			val description = cve.descriptions.firstOrNull { it.lang == "en" }?.value ?: ""
			val combinedLists = (cve.metrics.cvssMetricV31.orEmpty() + cve.metrics.cvssMetricV30.orEmpty() + cve.metrics.cvssMetricV2.orEmpty())
			var metricId = -1
			val metrics = combinedLists.map { dto ->
				metricId++
				VulnMetric(
					id = "${cve.id}.${metricId}",
					of_cve_id = cve.id,
					version = "CVSS ${dto.cvssData.version}",
					vectorString = dto.cvssData.vectorString,
					baseScore = dto.cvssData.baseScore,
					baseSeverity = dto.cvssData.baseSeverity.orEmpty()
				)
			}
			return@map VulnItemWithMetrics(
				item = VulnItem(
					cveId = cve.id,
					description = description,
					publishedDate = cve.published,
					lastModifiedDate = cve.lastModified,
					publishedDateUnix = LocalDateTime.parse(cve.published, DateTimeFormatter.ISO_DATE_TIME).toEpochSecond(ZoneOffset.UTC)
				),
				metrics = metrics,
			)
		}
	}
}