package com.willbanksy.vulnfind.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SettingsItemView(modifier: Modifier, title: String, description: String) {
	Row(
		modifier = modifier
	) {
		Column(
			modifier = Modifier
				.padding(16.dp)
				.fillMaxWidth()
		) {
			Text(text = title)
			Text(
				text = description,
				style = MaterialTheme.typography.body2,
				color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
			)
		}
	}
}