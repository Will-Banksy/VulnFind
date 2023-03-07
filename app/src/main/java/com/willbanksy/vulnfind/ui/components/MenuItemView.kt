package com.willbanksy.vulnfind.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun MenuSheetItemView(modifier: Modifier, icon: ImageVector, iconDesc: String, itemDesc: String) {
	Row(
		modifier = modifier
	) {
		Row(
			modifier = Modifier
				.padding(16.dp)
				.fillMaxWidth()
		) {
			Icon(
				imageVector = icon,
				contentDescription = iconDesc,
				tint = MaterialTheme.colors.onSurface
			)
			Spacer(modifier = Modifier.width(16.dp))
			Text(
				text = itemDesc,
				color = MaterialTheme.colors.onSurface,
			)
		}
	}
}