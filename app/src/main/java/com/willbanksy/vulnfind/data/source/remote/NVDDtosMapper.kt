package com.willbanksy.vulnfind.data.source.remote

import android.content.ContentValues.TAG
import android.util.Log
import com.willbanksy.vulnfind.data.VulnItem
import com.willbanksy.vulnfind.data.VulnItemWithMetrics
import com.willbanksy.vulnfind.data.VulnMetric

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
					id = "${metricId}.${metricId}",
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
				),
				metrics = metrics,
			)
		}
	}
}