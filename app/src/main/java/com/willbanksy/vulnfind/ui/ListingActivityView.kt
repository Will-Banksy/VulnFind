package com.willbanksy.vulnfind.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.willbanksy.vulnfind.R
import com.willbanksy.vulnfind.model.VulnListModel
import com.willbanksy.vulnfind.ui.components.TopBarView
import com.willbanksy.vulnfind.ui.components.VulnListView

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ListingActivityView(model: VulnListModel) {
	Surface(
		modifier = Modifier.fillMaxSize(),
		color = MaterialTheme.colors.background
	) {
		Scaffold(
			topBar = {
				TopBarView(label = stringResource(R.string.activity_listing_title), true)
			}
		) {
			val bottomPadding = WindowInsets.systemBars.asPaddingValues().calculateBottomPadding()
			Box {
				VulnListView(model, PaddingValues(bottom = bottomPadding))
			}
		}
	}
}