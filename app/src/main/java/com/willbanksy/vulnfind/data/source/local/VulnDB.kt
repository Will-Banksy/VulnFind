package com.willbanksy.vulnfind.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.willbanksy.vulnfind.data.VulnItem
import com.willbanksy.vulnfind.data.VulnMetric

@Database(entities = [VulnItem::class, VulnMetric::class], version = 1, exportSchema = false)
abstract class VulnDB : RoomDatabase() {
    abstract fun dao(): VulnDBDao
}