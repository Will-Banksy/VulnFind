package com.willbanksy.vulnfind.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

enum class VulnDataSource {
    SOURCE_DEFAULT,
    SOURCE_NVD,
    SOURCE_LOCALSTORAGE,
    SOURCE_NONE
}

@Entity
data class VulnItemState(
    @PrimaryKey val name: String,
    val description: String,
)

@Entity
data class VulnDBState(
    var vulns: List<VulnItemState> = listOf(),
    var source: VulnDataSource = VulnDataSource.SOURCE_NONE
)