package com.willbanksy.vulnfind.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.willbanksy.vulnfind.data.VulnItemState

@Database(entities = [VulnItemState::class], version = 1)
abstract class VulnDB : RoomDatabase() {
    abstract fun dao(): VulnDBDao
}