package com.willbanksy.vulnfind.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.willbanksy.vulnfind.data.VulnDBState
import com.willbanksy.vulnfind.data.VulnDataSource
import com.willbanksy.vulnfind.data.VulnItemState

class VulnDBModel : ViewModel() {
    var state by mutableStateOf(VulnDBState())
    
    fun populateFromLocalStorage() {
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
        
    }
}