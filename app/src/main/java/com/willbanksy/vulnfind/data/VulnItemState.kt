package com.willbanksy.vulnfind.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

// TODO: Probably want to enable FTS4 (Full text search 4)?

//enum class VulnDataSource {
//    SOURCE_DEFAULT,
//    SOURCE_NVD,
//    SOURCE_LOCALSTORAGE,
//    SOURCE_NONE
//}

@Entity(tableName = "VulnDB")
data class VulnItemState( // TODO: Add more fields to this. Make sure to update NVDDtosMapper.kt when I do
    @PrimaryKey @ColumnInfo("cve_id") val cveId: String = "",
    val description: String = "",
)

//data class VulnListState(
//    var vulns: List<VulnItemState> = listOf(),
//    var source: VulnDataSource = VulnDataSource.SOURCE_NONE
//)