package com.willbanksy.vulnfind.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.willbanksy.vulnfind.R
import com.willbanksy.vulnfind.model.VulnListModel
import com.willbanksy.vulnfind.ui.components.TopBarView
import com.willbanksy.vulnfind.ui.components.VulnDetailView

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun DetailsActivityView(model: VulnListModel, cveId: String) {
	Surface(
		modifier = Modifier.fillMaxSize(),
		color = MaterialTheme.colors.background
	) {
		Scaffold(
			topBar = {
				TopBarView(label = stringResource(R.string.activity_details_title), true)
			}
		) {
			VulnDetailView(model, cveId)
		}
	}
}