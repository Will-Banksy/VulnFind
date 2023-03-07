package com.willbanksy.vulnfind.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.willbanksy.vulnfind.R
import com.willbanksy.vulnfind.ui.components.TopBarView

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun SettingsActivityView() {
	Surface(
		modifier = Modifier.fillMaxSize(),
		color = MaterialTheme.colors.background
	) {
		Scaffold(
			topBar = {
				TopBarView(label = stringResource(R.string.activity_settings_title), true)
			}
		) {
		}
	}
}