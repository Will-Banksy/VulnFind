package com.willbanksy.vulnfind.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.willbanksy.vulnfind.model.VulnListModel

@Composable
fun VulnListView(model: VulnListModel) {
    val vulns = model.vulnsStream.collectAsState(initial = emptyList())
    Box(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxSize()
    ) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(vulns.value) { item ->
                VulnItem(title = item.cveId, description = item.description)
            }
        }
    }
}