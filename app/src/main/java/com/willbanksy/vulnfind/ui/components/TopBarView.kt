package com.willbanksy.vulnfind.ui.components

import android.app.Activity
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.willbanksy.vulnfind.R

@Composable
fun TopBarView(label: String, showBackButton: Boolean = false) {
	val topPadding = WindowInsets.systemBars.asPaddingValues().calculateTopPadding()
	TopAppBar(
		contentPadding = PaddingValues(top = topPadding, start = 8.dp, end = 8.dp),
		elevation = 0.dp,
		backgroundColor = MaterialTheme.colors.surface
	) {
		if(showBackButton) {
			val activity = (LocalContext.current as? Activity)
			IconButton(onClick = {
				activity?.finish()
			}) {
				Icon(
					imageVector = Icons.Filled.ArrowBack,
					contentDescription = stringResource(R.string.view_topbar_back_icon_name),
					tint = MaterialTheme.colors.onSurface
				)
			}
		}
		Text(
			text = label,
			style = MaterialTheme.typography.h6,
			color = MaterialTheme.colors.onSurface,
			modifier = Modifier.padding(start = 8.dp)
		)
	}
}