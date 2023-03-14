package com.willbanksy.vulnfind.data

import androidx.room.*

// TODO: Move this shit to be a LocalDataSource specific thing, and will need a mapper to transform that into a standard format for dealing with in the application

// TODO: Probably want to enable FTS4 (Full text search 4)?

//enum class VulnDataSource {
//    SOURCE_DEFAULT,
//    SOURCE_NVD,
//    SOURCE_LOCALSTORAGE,
//    SOURCE_NONE
//}

@Entity(tableName = "VulnDB")
data class VulnItem( // TODO: Add more fields to this. Make sure to update NvdDtosMapper.kt when I do
	@PrimaryKey @ColumnInfo("cve_id") val cveId: String = "",
	val description: String = "",
	@ColumnInfo("published_date") val publishedDate: String = "",
	@ColumnInfo("last_modified_date") val lastModifiedDate: String = "",
	/// The published date in Unix time to speed up sorting by published date
	@ColumnInfo("published_date_unix") val publishedDateUnix: Long
//	val references: List<VulnReference>
)

@Entity(tableName = "VulnMetrics")
data class VulnMetric(
	@PrimaryKey val id: String = "",
	val of_cve_id: String = "",
	val version: String = "",
	@ColumnInfo("vector_string") val vectorString: String = "",
	@ColumnInfo("base_score") val baseScore: Float = 0f,
	@ColumnInfo("base_severity") val baseSeverity: String = ""
)

data class VulnItemWithMetrics(
	@Embedded val item: VulnItem,
	@Relation(
		parentColumn = "cve_id",
		entityColumn = "of_cve_id",
	)
	val metrics: List<VulnMetric>
)

//@Entity(tableName = "VulnReferences")
//data class VulnReference(
//	@PrimaryKey(autoGenerate = true) val id: Int = 0,
//	val url: String,
//	val source: String,
//	val tags: List<String> = emptyList()
//)

//data class VulnListState(
//    var vulns: List<VulnItemState> = listOf(),
//    var source: VulnDataSource = VulnDataSource.SOURCE_NONE
//)