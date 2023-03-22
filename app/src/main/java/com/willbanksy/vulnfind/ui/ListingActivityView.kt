package com.willbanksy.vulnfind.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.willbanksy.vulnfind.R
import com.willbanksy.vulnfind.models.MainViewModel
import com.willbanksy.vulnfind.ui.components.DefaultScaffoldView
import com.willbanksy.vulnfind.ui.components.TopBarView
import com.willbanksy.vulnfind.ui.components.VulnListView

@OptIn(ExperimentalMaterialApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ListingActivityView(model: MainViewModel) {
	val bottomPadding = WindowInsets.systemBars.asPaddingValues().calculateBottomPadding()
	Surface(
		modifier = Modifier.fillMaxSize(),
		color = MaterialTheme.colors.background
	) {
		DefaultScaffoldView(topBarLabel = stringResource(R.string.activity_listing_title), topBarShowBack = true) {
			VulnListView(model, PaddingValues(bottom = bottomPadding))
		}
	}
}