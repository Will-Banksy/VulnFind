package com.willbanksy.vulnfind

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.room.Room
import com.willbanksy.vulnfind.data.source.VulnRepository
import com.willbanksy.vulnfind.data.source.local.VulnDB
import com.willbanksy.vulnfind.data.source.local.VulnLocalDataSource
import com.willbanksy.vulnfind.data.source.remote.VulnRemoteDataSource
import com.willbanksy.vulnfind.model.VulnListModel
import com.willbanksy.vulnfind.ui.AboutActivityView
import com.willbanksy.vulnfind.ui.SettingsActivityView
import com.willbanksy.vulnfind.ui.theme.VulnFindTheme

class AboutActivity : ComponentActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContent {
			VulnFindTheme {
				AboutActivityView()
			}
		}
	}
}