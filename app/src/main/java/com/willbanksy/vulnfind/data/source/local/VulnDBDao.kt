package com.willbanksy.vulnfind.data.source.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.willbanksy.vulnfind.data.VulnItemState
import kotlinx.coroutines.flow.Flow

@Dao
interface VulnDBDao {
    @Query("SELECT * FROM VulnDB")
    fun getAll(): List<VulnItemState>
    
    @Query("SELECT * FROM VulnDB")
    fun getAllLive(): LiveData<List<VulnItemState>>
    
    @Query("SELECT * FROM VulnDB WHERE name LIKE :searchTerm")
    fun search(searchTerm: String): List<VulnItemState>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vulns: List<VulnItemState>)
    
    @Delete
    fun deleteVuln(vuln: VulnItemState)
}