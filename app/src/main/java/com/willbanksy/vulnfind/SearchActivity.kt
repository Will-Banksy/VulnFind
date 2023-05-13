package com.willbanksy.vulnfind

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.room.Room
import com.willbanksy.vulnfind.data.source.VulnRepository
import com.willbanksy.vulnfind.data.source.local.VulnDB
import com.willbanksy.vulnfind.data.source.local.VulnLocalDataSource
import com.willbanksy.vulnfind.data.source.remote.VulnRemoteDataSource
import com.willbanksy.vulnfind.models.MainViewModel
import com.willbanksy.vulnfind.ui.SearchActivityView
import com.willbanksy.vulnfind.ui.theme.VulnFindTheme

class SearchActivity : ComponentActivity() {
	// TODO: find some way to scope these to the activity or application or something so that they don't get recreated when the device is rotated etc
	private lateinit var repository: VulnRepository
	private lateinit var vulnDB: VulnDB
	private lateinit var model: MainViewModel
	
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContent {
			VulnFindTheme {
				vulnDB = Room.databaseBuilder(this, VulnDB::class.java, "VulnDB")
					.enableMultiInstanceInvalidation()
					.build()
				repository = VulnRepository(VulnRemoteDataSource(), VulnLocalDataSource(vulnDB.dao()))
				model = MainViewModel(repository)

				SearchActivityView(model)
			}
		}
	}
}