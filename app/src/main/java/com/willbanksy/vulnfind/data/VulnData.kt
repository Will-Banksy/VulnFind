package com.willbanksy.vulnfind.data

data class VulnDataItem(
	val cveId: String,
	val description: String,
	val publishedDate: String,
	val lastModifiedDate: String,
	val sourceId: String,
	val bookmarked: Boolean = false,
	val metrics: List<VulnDataItemMetric>,
	val references: List<VulnDataItemReference>
)

data class VulnDataItemMetric(
	val version: String,
	val vectorString: String,
	val baseScore: Float,
	val baseSeverity: String,
)

data class VulnDataItemReference(
	val url: String,
	val source: String,
	val tags: List<String>
)