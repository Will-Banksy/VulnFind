package com.willbanksy.vulnfind.ui

import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import com.willbanksy.vulnfind.DetailsActivity
import com.willbanksy.vulnfind.model.VulnListModel

@Composable
fun VulnListView(model: VulnListModel) {
	val vulns = model.vulnsStream.collectAsState(initial = emptyList())
	Box(
		modifier = Modifier
			.fillMaxSize()
	) {
		LazyColumn(
			verticalArrangement = Arrangement.spacedBy(8.dp),
			contentPadding = PaddingValues(8.dp)
		) {
			items(vulns.value) { item ->
				val intent = Intent(LocalContext.current, DetailsActivity::class.java).apply {
					putExtra("cveId", item.item.cveId)
				}
				val context = LocalContext.current
				VulnItemCardView(
					modifier = Modifier.clickable {
						startActivity(context, intent, null)
					},
					vuln = item
				)
			}
		}
	}
}