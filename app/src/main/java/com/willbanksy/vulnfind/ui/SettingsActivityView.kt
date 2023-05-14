package com.willbanksy.vulnfind.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.willbanksy.vulnfind.R
import com.willbanksy.vulnfind.models.MainViewModel
import com.willbanksy.vulnfind.ui.components.DefaultScaffoldView
import com.willbanksy.vulnfind.ui.content_views.SettingsView

@OptIn(ExperimentalMaterialApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun SettingsActivityView(model: MainViewModel) {
	Surface(
		modifier = Modifier.fillMaxSize(),
		color = MaterialTheme.colors.background
	) {
		DefaultScaffoldView(topBarLabel = stringResource(R.string.activity_settings_title), topBarShowBack = true) {
			SettingsView(model)
		}
	}
}