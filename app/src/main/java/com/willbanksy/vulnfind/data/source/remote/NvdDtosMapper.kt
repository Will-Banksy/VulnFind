package com.willbanksy.vulnfind.data.source.remote

import com.willbanksy.vulnfind.data.VulnDataItem
import com.willbanksy.vulnfind.data.VulnDataItemMetric

fun mapToItems(listingDto: NvdCveListingDto?): List<VulnDataItem> {
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
				VulnDataItemMetric(
					version = "CVSS ${dto.cvssData.version}",
					vectorString = dto.cvssData.vectorString,
					baseScore = dto.cvssData.baseScore,
					baseSeverity = dto.cvssData.baseSeverity.orEmpty()
				)
			}
			return@map VulnDataItem(
				cveId = cve.id,
				description = description,
				publishedDate = cve.published,
				lastModifiedDate = cve.lastModified,
				metrics = metrics,
			)
		}
	}
}