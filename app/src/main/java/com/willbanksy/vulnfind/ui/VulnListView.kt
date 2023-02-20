package com.willbanksy.vulnfind.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.willbanksy.vulnfind.model.VulnDBModel

@Composable
fun VulnListView(model: VulnDBModel) {
    Box(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxSize()
    ) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(model.state.vulns) { item ->
                VulnItem(title = item.name, description = item.description)
            }
        }
    }
}