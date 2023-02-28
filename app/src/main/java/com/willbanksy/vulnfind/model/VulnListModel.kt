package com.willbanksy.vulnfind.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.willbanksy.vulnfind.data.VulnItemState
import com.willbanksy.vulnfind.data.source.VulnRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.job
import kotlinx.coroutines.launch

class VulnListModel(
	private val vulnRepository: VulnRepository
) : ViewModel() {
	var vulnsStream: Flow<List<VulnItemState>> = vulnRepository.vulns
	
	fun refreshId(cveId: String) {
		viewModelScope.launch { 
			vulnRepository.refreshId(cveId)
		}
	}
	
	fun getById(cveId: String): Flow<VulnItemState?> {
		return vulnRepository.getVulnStream(cveId)
	}
	
	fun insertDefault() {
		viewModelScope.launch { 
			vulnRepository.manuallySaveToDB(
				listOf(
					VulnItemState("CVE-2023-1337", "\uD83D\uDE0E"),
					VulnItemState("CVE-2023-8008", "uwu"),
					VulnItemState("CVE-2023-1234", "Error ipsa et quia debitis. Dolor ut eveniet dignissimos id. Repellat necessitatibus esse et sit ex harum. Quia iusto officiis est quo beatae itaque reiciendis fuga.\n" +
							"\n" +
							"Ipsam deserunt a quis. Possimus et mollitia numquam quia. Iste odio dolorem et doloremque. Doloremque illo aliquam neque sint amet error consequatur. Expedita enim praesentium iure soluta qui hic optio. Velit et dignissimos nemo.")
				)
			)
		}
	}
}