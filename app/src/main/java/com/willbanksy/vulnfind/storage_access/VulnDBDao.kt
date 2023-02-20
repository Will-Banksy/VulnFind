package com.willbanksy.vulnfind.storage_access

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.willbanksy.vulnfind.data.VulnItemState

@Dao
interface VulnDBDao {
    @Query("SELECT * FROM VulnDB")
    fun getAll(): List<VulnItemState>
    
    @Query("SELECT * FROM VulnDB WHERE name LIKE :searchTerm")
    fun search(searchTerm: String): List<VulnItemState>
    
    @Insert
    fun insertAll(vararg vulns: VulnItemState)
}