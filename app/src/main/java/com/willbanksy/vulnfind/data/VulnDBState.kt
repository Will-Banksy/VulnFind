package com.willbanksy.vulnfind.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

// TODO: Rename some of this shit

enum class VulnDataSource {
    SOURCE_DEFAULT,
    SOURCE_NVD,
    SOURCE_LOCALSTORAGE,
    SOURCE_NONE
}

@Entity(tableName = "VulnDB")
data class VulnItemState(
    @PrimaryKey val name: String,
    val description: String,
)

@Entity(tableName = "VulnDBList")
data class VulnDBState( // TODO: Probably want to enable FTS4 (Full text search 4)?
    var vulns: List<VulnItemState> = listOf(),
    @Ignore var source: VulnDataSource = VulnDataSource.SOURCE_NONE
)