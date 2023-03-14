package com.willbanksy.vulnfind.data

data class VulnDataItem(
	val cveId: String,
	val description: String,
	val publishedDate: String,
	val lastModifiedDate: String,
	val metrics: List<VulnDataItemMetric>
)

data class VulnDataItemMetric(
	val version: String,
	val vectorString: String,
	val baseScore: Float,
	val baseSeverity: String,
)