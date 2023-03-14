package com.willbanksy.vulnfind

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.willbanksy.vulnfind.ui.AboutActivityView
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