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
import androidx.compose.ui.unit.dp
import com.willbanksy.vulnfind.model.VulnListModel

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun DetailsActivityView(model: VulnListModel, cveId: String) {
	val vulnStream = remember {
		model.getById(cveId)
	}

	val vuln = vulnStream.collectAsState(initial = null).value
	val vulnTitle = vuln?.item?.cveId ?: "Not Found"
	Surface(
		modifier = Modifier.fillMaxSize(),
		color = MaterialTheme.colors.background
	) {
		Scaffold(
			topBar = {
				val topPadding = WindowInsets.systemBars.asPaddingValues().calculateTopPadding()
				TopAppBar(
					contentPadding = PaddingValues(top = topPadding, start = 8.dp, end = 8.dp),
					elevation = 0.dp
				) {
					Text(
						text = "Vulnerability Details",
						style = MaterialTheme.typography.h6,
						color = MaterialTheme.colors.onSurface,
						modifier = Modifier.padding(start =  8.dp)
					)
				}
			}
		) {
			Box(
				modifier = Modifier
					.fillMaxSize()
					.padding(16.dp)
			) {
				Column {
					Text(
						text = vulnTitle,
						style = MaterialTheme.typography.h4
					)
					if(vuln != null) {
						Spacer(modifier = Modifier.height(16.dp))
						Text(
							text = vuln.item.description
						)
						Spacer(modifier = Modifier.height(16.dp))
						Text(
							text = "Published: ${vuln.item.publishedDate}"
						)
						Text(
							text = "Modified: ${vuln.item.lastModifiedDate}"
						)
						Spacer(modifier = Modifier.height(16.dp))
						Text(text = "Number of metrics found: ${vuln.metrics.size}")
						LazyColumn(
							verticalArrangement = Arrangement.spacedBy(8.dp),
							contentPadding = PaddingValues(8.dp)
						) {
							this.items(vuln.metrics) { metric ->
								Text(text = "Found metric: ${metric.version}")
							}
						}
					}
				}
			}
		}
	}
}