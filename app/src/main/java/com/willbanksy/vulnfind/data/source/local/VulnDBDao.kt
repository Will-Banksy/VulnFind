package com.willbanksy.vulnfind.data.source.local

import androidx.paging.PagingSource
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface VulnDBDao {
//	@Transaction
//    @Query("SELECT * FROM VulnDB")
//    fun getAll(): List<VulnItemWithMetrics>
//
//	@Transaction
//    @Query("SELECT * FROM VulnDB WHERE cve_id = :cveId")
//    fun getById(cveId: String): VulnItemWithMetrics?

	@Transaction
    @Query("SELECT * FROM VulnDB ORDER BY published_date_unix DESC")
    fun observeAll(): PagingSource<Int, VulnDBVulnWithMetricsAndReferencesDto>

	@Transaction
	@Query("SELECT * FROM VulnDB WHERE strftime(:dateCmpFormat, datetime(published_date_unix, 'unixepoch')) = strftime(:dateCmpFormat, datetime(:unixTime, 'unixepoch')) AND primary_metric_version <> :primaryMetricFilterOut AND primary_metric_base_score BETWEEN :minSeverity AND :maxSeverity AND description LIKE :descriptionContentFilter ORDER BY published_date_unix DESC")
	fun observeAllFilteredMetrics(unixTime: Long, dateCmpFormat: String, minSeverity: Float, maxSeverity: Float, descriptionContentFilter: String, primaryMetricFilterOut: String): PagingSource<Int, VulnDBVulnWithMetricsAndReferencesDto>

	@Transaction
    @Query("SELECT * FROM VulnDB WHERE cve_id = :cveId")
    fun observeById(cveId: String): Flow<VulnDBVulnWithMetricsAndReferencesDto?>
	
//	@Query("SELECT * FROM VulnMetrics")
//	fun getAllMetrics(): Flow<List<VulnMetric>>

//	@Transaction
//    @Query("SELECT * FROM VulnDB WHERE cve_id LIKE :searchTerm")
//    fun search(searchTerm: String): List<VulnItemWithMetrics>

//	@Transaction
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    fun insertAll(vulns: List<VulnItem>, metrics: List<List<VulnMetric>>)

//	@Transaction
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    fun insert(vuln: VulnDBVulnDto, metrics: List<VulnDBMetricDto>)
	
	@Insert(onConflict = OnConflictStrategy.REPLACE)
	fun insertAllVulns(vulns: List<VulnDBVulnDto>)
	
	@Insert(onConflict = OnConflictStrategy.REPLACE)
	fun insertAllMetrics(metrics: List<VulnDBMetricDto>)
	
	@Insert(onConflict = OnConflictStrategy.REPLACE)
	fun insertAllReferences(refs: List<VulnDBReferenceDto>)
	
	@Update
	fun updateVuln(vuln: VulnDBVulnDto)
	
//	fun insertAllMetrics()

//	@Transaction
//    @Delete
//    fun delete(vuln: VulnItemWithMetrics)
}