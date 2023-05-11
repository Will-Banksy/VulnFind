package com.willbanksy.vulnfind.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun HorizontalItemView(
	modifier: Modifier = Modifier,
	padding: Dp = 16.dp,
	content: @Composable RowScope.() -> Unit
) {
	Row(
		modifier = modifier
	) {
		Row(
			modifier = Modifier
				.padding(padding)
				.fillMaxWidth()
		) {
			content()
		}
	}
}

@Composable
fun HorizontalItemView(
	icon: ImageVector,
	iconDesc: String,
	itemDesc: String,
	modifier: Modifier = Modifier,
	padding: Dp = 16.dp,
) {
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