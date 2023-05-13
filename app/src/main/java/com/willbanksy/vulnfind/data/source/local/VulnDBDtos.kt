package com.willbanksy.vulnfind.data.source.local

import androidx.room.*

@Entity(tableName = "VulnDB")
data class VulnDBVulnDto( // TODO: Add more fields to this. Make sure to update VulnDBDtosMapper.kt when I do
	@PrimaryKey @ColumnInfo("cve_id") val cveId: String = "",
	val description: String = "",
	@ColumnInfo("published_date") val publishedDate: String = "",
	@ColumnInfo("last_modified_date") val lastModifiedDate: String = "",
	/// The published date in Unix time to speed up sorting by published date
	@ColumnInfo("published_date_unix") val publishedDateUnix: Long,
	@ColumnInfo("source_id") val sourceId: String,
	@ColumnInfo("bookmarked") val bookmarked: Boolean = false,
	@Embedded("primary_metric_") val primaryMetric: VulnDBMetricDto,
//	val references: List<VulnReference>
)

@Entity(tableName = "VulnMetrics")
data class VulnDBMetricDto(
	@PrimaryKey val id: String = "",
	@ColumnInfo("of_cve_id") val ofCveId: String = "",
	val version: String = "",
	@ColumnInfo("vector_string") val vectorString: String = "",
	@ColumnInfo("base_score") val baseScore: Float = 0f,
	@ColumnInfo("base_severity") val baseSeverity: String = ""
)

data class VulnDBVulnWithMetricsAndReferencesDto(
	@Embedded val item: VulnDBVulnDto,
	@Relation(
		parentColumn = "cve_id",
		entityColumn = "of_cve_id",
	)
	val metrics: List<VulnDBMetricDto>,
	@Relation(
		parentColumn = "cve_id",
		entityColumn = "of_cve_id"
	)
	val references: List<VulnDBReferenceDto>
)

@Entity(tableName = "VulnReferences")
data class VulnDBReferenceDto(
	@PrimaryKey(autoGenerate = true) val id: Int = 0,
	@ColumnInfo("of_cve_id") val ofCveId: String,
	val url: String,
	val source: String,
	val tags: String // List stored as a ;-separated string
)