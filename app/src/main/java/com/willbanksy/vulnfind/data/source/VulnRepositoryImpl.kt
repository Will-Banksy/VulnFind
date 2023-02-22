package com.willbanksy.vulnfind.data.source

import androidx.lifecycle.LiveData
import com.willbanksy.vulnfind.data.VulnItemState
import com.willbanksy.vulnfind.data.source.VulnRepository
import com.willbanksy.vulnfind.data.source.local.VulnDBDao

class VulnRepositoryImpl(
    private val dao: VulnDBDao
): VulnRepository {
    override suspend fun getAll(): List<VulnItemState> {
        return dao.getAll()
    }

    override fun getAllLive(): LiveData<List<VulnItemState>> {
        return dao.getAllLive()
    }

    override suspend fun insertAll(vulns: List<VulnItemState>) {
        dao.insertAll(vulns)
    }
}