package com.willbanksy.vulnfind.data.source.local

import androidx.room.*
import com.willbanksy.vulnfind.data.VulnItem
import com.willbanksy.vulnfind.data.VulnItemWithMetrics
import com.willbanksy.vulnfind.data.VulnMetric
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
    fun observeAll(): Flow<List<VulnItemWithMetrics>>

	@Transaction
    @Query("SELECT * FROM VulnDB WHERE cve_id = :cveId")
    fun observeById(cveId: String): Flow<VulnItemWithMetrics?>
	
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
    fun insert(vuln: VulnItem, metrics: List<VulnMetric>)

//	@Transaction
//    @Delete
//    fun delete(vuln: VulnItemWithMetrics)
}