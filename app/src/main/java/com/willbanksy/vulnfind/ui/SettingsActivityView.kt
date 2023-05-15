package com.willbanksy.vulnfind.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.willbanksy.vulnfind.models.MainViewModel
import com.willbanksy.vulnfind.ui.content_views.SettingsView

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun SettingsActivityView(model: MainViewModel) {
	Surface(
		modifier = Modifier.fillMaxSize(),
		color = MaterialTheme.colors.background
	) {
		SettingsView(model)
	}
}