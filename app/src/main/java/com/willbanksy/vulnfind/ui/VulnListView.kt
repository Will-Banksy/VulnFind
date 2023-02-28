package com.willbanksy.vulnfind.ui

import android.content.Intent
import android.util.Log
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import com.willbanksy.vulnfind.DetailsActivity
import com.willbanksy.vulnfind.model.VulnListModel

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun VulnListView(model: VulnListModel) {
	val vulns = model.vulnsStream.collectAsState(initial = emptyList())
	Box(
		modifier = Modifier
//            .padding(8.dp)
			.fillMaxSize()
	) {
		LazyColumn(
			verticalArrangement = Arrangement.spacedBy(8.dp),
			contentPadding = PaddingValues(8.dp)
		) {
			items(vulns.value) { item ->
				val intent = Intent(LocalContext.current, DetailsActivity::class.java).apply {
					putExtra("cveId", item.cveId)
				}
				val context = LocalContext.current
//				val expanded = remember { mutableStateOf(false) }
				VulnItemCardView(
					modifier = Modifier.clickable {
						startActivity(context, intent, null)
//						expanded.value = true
						Log.d("CONTENT ANIM", "Expanded Vuln Card View")
					},
					title = item.cveId,
					description = item.description
				)
			}
		}
	}
}