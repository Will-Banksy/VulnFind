package com.willbanksy.vulnfind.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.willbanksy.vulnfind.model.VulnListModel

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun DetailsActivityView(model: VulnListModel, cveId: String) {
	Surface(
		modifier = Modifier.fillMaxSize(),
		color = MaterialTheme.colors.background
	) {
		Scaffold {
			val vulnStream = remember {
				model.getById(cveId)
			}
			
			val vuln = vulnStream.collectAsState(initial = null).value
			val text = vuln?.cveId ?: "Not Found"
			val topPadding = WindowInsets.systemBars.asPaddingValues().calculateTopPadding()
			val bottomPadding = WindowInsets.systemBars.asPaddingValues().calculateBottomPadding()
			Box(
				modifier = Modifier.fillMaxSize().padding(8.dp)
			) {
				Column(
					modifier = Modifier.padding(top = topPadding, bottom = bottomPadding)
				) {
					Text(
						text = text,
						style = MaterialTheme.typography.h4
					)
					if(vuln != null) {
						Spacer(modifier = Modifier.height(16.dp))
						Text(
							text = vuln.description
						)
					}
				}
			}
		}
	}
}