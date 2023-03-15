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
    fun observeAll(): PagingSource<Int, VulnDBVulnWithMetricsDto>

	@Transaction
    @Query("SELECT * FROM VulnDB WHERE cve_id = :cveId")
    fun observeById(cveId: String): Flow<VulnDBVulnWithMetricsDto?>
	
//	@Query("SELECT * FROM VulnMetrics")
//	fun getAllMetrics(): Flow<List<VulnMetric>>

//	@Transaction
//    @Query("SELECT * FROM VulnDB WHERE cve_id LIKE :searchTerm")
//    fun search(searchTerm: String): List<VulnItemWithMetrics>

//	@Transaction
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    fun insertAll(vulns: List<VulnItem>, metrics: List<List<VulnMetric>>)

	@Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vuln: VulnDBVulnDto, metrics: List<VulnDBMetricDto>)

//	@Transaction
//    @Delete
//    fun delete(vuln: VulnItemWithMetrics)
}