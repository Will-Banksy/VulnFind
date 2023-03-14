package com.willbanksy.vulnfind.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [VulnDBVulnDto::class, VulnDBMetricDto::class], version = 1, exportSchema = false)
abstract class VulnDB : RoomDatabase() {
    abstract fun dao(): VulnDBDao
}