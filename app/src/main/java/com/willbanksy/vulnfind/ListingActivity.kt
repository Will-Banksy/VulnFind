package com.willbanksy.vulnfind

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.room.Room
import com.willbanksy.vulnfind.data.source.SettingsRepository
import com.willbanksy.vulnfind.data.source.VulnRepository
import com.willbanksy.vulnfind.data.source.local.SettingsLocalDataSource
import com.willbanksy.vulnfind.data.source.local.VulnDB
import com.willbanksy.vulnfind.data.source.local.VulnLocalDataSource
import com.willbanksy.vulnfind.data.source.local.settingsDataStore
import com.willbanksy.vulnfind.data.source.remote.VulnRemoteDataSource
import com.willbanksy.vulnfind.models.MainViewModel
import com.willbanksy.vulnfind.ui.ListingActivityView
import com.willbanksy.vulnfind.ui.theme.VulnFindTheme

class ListingActivity : ComponentActivity() {
	private lateinit var model: MainViewModel

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContent {
			VulnFindTheme {
				val vulnDB = Room.databaseBuilder(this, VulnDB::class.java, "VulnDB").enableMultiInstanceInvalidation().build()
				val vulnRepository = VulnRepository(VulnRemoteDataSource(), VulnLocalDataSource(vulnDB.dao()))
				val settingsRepository = SettingsRepository(SettingsLocalDataSource(settingsDataStore))
				model = MainViewModel(vulnRepository, settingsRepository)
				Log.d("Activity Creation", "Activity recreated rn")

				ListingActivityView(model)
			}
		}
	}
}