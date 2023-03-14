package com.willbanksy.vulnfind.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.willbanksy.vulnfind.models.MainViewModel

@Composable
fun VulnDetailView(model: MainViewModel, cveId: String) {
	val vulnStream = remember {
		model.getById(cveId)
	}
	val vuln = vulnStream.collectAsState(initial = null).value
	val vulnTitle = vuln?.cveId ?: "Not Found"
	
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
					text = vuln.description
				)
				Spacer(modifier = Modifier.height(16.dp))
				Text(
					text = "Published: ${vuln.publishedDate}"
				)
				Text(
					text = "Modified: ${vuln.lastModifiedDate}"
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