package com.willbanksy.vulnfind

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.room.Room
import com.willbanksy.vulnfind.data.source.VulnRepository
import com.willbanksy.vulnfind.data.source.local.VulnDB
import com.willbanksy.vulnfind.data.source.local.VulnLocalDataSource
import com.willbanksy.vulnfind.data.source.remote.VulnRemoteDataSource
import com.willbanksy.vulnfind.model.VulnListModel
import com.willbanksy.vulnfind.ui.DetailsActivityView
import com.willbanksy.vulnfind.ui.theme.VulnFindTheme

class DetailsActivity : ComponentActivity() {
	// TODO: find some way to scope these to the activity or application or something so that they don't get recreated when the device is rotated etc
	private lateinit var repository: VulnRepository
	private lateinit var vulnDB: VulnDB
	private lateinit var model: VulnListModel
	
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContent {
			VulnFindTheme {
				vulnDB = Room.databaseBuilder(this, VulnDB::class.java, "VulnDB").build()
				repository = VulnRepository(VulnRemoteDataSource(), VulnLocalDataSource(vulnDB.dao()))
				model = VulnListModel(repository)
				
				// Get intent data
				val b = intent.extras
				var argument = ""
				if(b != null) {
					argument = b.getString("cveId") ?: ""
				}
				
				DetailsActivityView(model, argument)
			}
		}
	}
}
