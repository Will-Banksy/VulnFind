package com.willbanksy.vulnfind.data.source.remote

import com.willbanksy.vulnfind.data.VulnItemState

fun mapToItem(listingDto: CveListingDto?): VulnItemState? {
	return listingDto.let { cveListing ->
		if(cveListing == null) {
			return@let null
		}
		val cve = cveListing.vulnerabilities[0].cve
		VulnItemState(
			cveId = cve.id,
			description = cve.descriptions.firstOrNull { it.lang == "en" }?.value ?: ""
		)
	}
}