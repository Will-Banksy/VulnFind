package com.willbanksy.vulnfind.model

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.willbanksy.vulnfind.data.VulnDBState
import com.willbanksy.vulnfind.data.VulnDataSource
import com.willbanksy.vulnfind.data.VulnItemState
import com.willbanksy.vulnfind.data.source.local.VulnDB
import com.willbanksy.vulnfind.data.source.VulnRepository
import com.willbanksy.vulnfind.data.source.VulnRepositoryImpl
import kotlinx.coroutines.launch

class VulnDBModel(context: Context): ViewModel() { // TODO: Architectural overhaul
    var state by mutableStateOf(VulnDBState())
    private val vulnDb: VulnDB
    private val vulnRepo: VulnRepository
    var vulnsLive: LiveData<List<VulnItemState>>? = null

    init {
        vulnDb = Room.databaseBuilder(context, VulnDB::class.java, "VulnDB").build()
        vulnRepo = VulnRepositoryImpl(vulnDb.dao())
        viewModelScope.launch {
            vulnsLive = vulnRepo.getAllLive()
        }
    }
    
//    class Factory(context: Context): ViewModelProvider.Factory {
//    }
    
//    fun initialiseDB(context: Context) {
//    }
    
    fun populateFromDB() {
        viewModelScope.launch { 
            state = state.copy(vulns = vulnRepo.getAll())
        }
    }
    
    fun populateDefault() {
        state = state.copy(vulns = listOf(
            VulnItemState("CVE-2023-1337", "\uD83D\uDE0E"),
            VulnItemState("CVE-2023-8008", "uwu"),
            VulnItemState("CVE-2023-8008", "uwu"),
            VulnItemState("CVE-2023-8008", "uwu"),
            VulnItemState("CVE-2023-8008", "uwu"),
            VulnItemState("CVE-2023-8008", "uwu"),
            VulnItemState("CVE-2023-8008", "uwu"),
            VulnItemState("CVE-2023-8008", "uwu"),
            VulnItemState("CVE-2023-8008", "uwu"),
            VulnItemState("CVE-2023-8008", "uwu"),
            VulnItemState("CVE-2023-8008", "uwu"),
            VulnItemState("CVE-2023-8008", "uwu"),
            VulnItemState("CVE-2023-8008", "uwu"),
            VulnItemState("CVE-2023-8008", "uwu"),
            VulnItemState("CVE-2023-8008", "uwu"),
            VulnItemState("CVE-2023-8008", "uwu"),
            VulnItemState("CVE-2023-8008", "uwu"),
            VulnItemState("CVE-2023-8008", "uwu"),
            VulnItemState("CVE-2023-8008", "uwu"),
            VulnItemState("CVE-2023-8008", "uwu"),
            VulnItemState("CVE-2023-8008", "uwu"),
            VulnItemState("CVE-2023-8008", "uwu"),
            VulnItemState("CVE-2023-8008", "uwu"),
            VulnItemState("CVE-2023-8008", "uwu"),
            VulnItemState("CVE-2023-8008", "uwu"),
            VulnItemState("CVE-2023-8008", "uwu"),
            VulnItemState("CVE-2023-8008", "uwu"),
            VulnItemState("CVE-2023-8008", "uwu"),
            VulnItemState("CVE-2023-8008", "uwu"),
            VulnItemState("CVE-2023-8008", "uwu"),
            VulnItemState("CVE-2023-8008", "uwu"),
            VulnItemState("CVE-2023-1234", "Error ipsa et quia debitis. Dolor ut eveniet dignissimos id. Repellat necessitatibus esse et sit ex harum. Quia iusto officiis est quo beatae itaque reiciendis fuga.\n" +
                    "\n" +
                    "Ipsam deserunt a quis. Possimus et mollitia numquam quia. Iste odio dolorem et doloremque. Doloremque illo aliquam neque sint amet error consequatur. Expedita enim praesentium iure soluta qui hic optio. Velit et dignissimos nemo.")
        ), source = VulnDataSource.SOURCE_DEFAULT)
    }
    
    fun save() {
        viewModelScope.launch { 
            vulnRepo.insertAll(state.vulns)
        }
    }
}