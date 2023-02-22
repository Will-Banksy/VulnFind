package com.willbanksy.vulnfind.data.source.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.willbanksy.vulnfind.data.VulnItemState
import kotlinx.coroutines.flow.Flow

@Dao
interface VulnDBDao {
    @Query("SELECT * FROM VulnDB")
    fun getAll(): List<VulnItemState>

    @Query("SELECT * FROM VulnDB WHERE cve_id = :cveId")
    fun getById(cveId: String): VulnItemState?
    
    @Query("SELECT * FROM VulnDB")
    fun observeAll(): Flow<List<VulnItemState>>
    
    @Query("SELECT * FROM VulnDB WHERE cve_id = :cveId")
    fun observeById(cveId: String): Flow<VulnItemState?>
    
    @Query("SELECT * FROM VulnDB WHERE cve_id LIKE :searchTerm")
    fun search(searchTerm: String): List<VulnItemState>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vulns: List<VulnItemState>)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vuln: VulnItemState)
    
    @Delete
    fun delete(vuln: VulnItemState)
}