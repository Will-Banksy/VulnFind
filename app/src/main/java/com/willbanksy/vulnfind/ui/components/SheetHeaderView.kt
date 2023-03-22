package com.willbanksy.vulnfind.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.willbanksy.vulnfind.R
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MenuSheetHeaderView(sheetState: ModalBottomSheetState, headerText: String) {
	Row(
		modifier = Modifier
			.background(MaterialTheme.colors.surface)
			.fillMaxWidth(),
		verticalAlignment = Alignment.CenterVertically
	) {
		Text(
			text = headerText,
			style = MaterialTheme.typography.h6,
			color = MaterialTheme.colors.onSurface,
			modifier = Modifier.padding(16.dp)
		)
		Spacer(modifier = Modifier.weight(1f))
		val coroutineScope = rememberCoroutineScope()
		IconButton(onClick = {
			coroutineScope.launch {
				sheetState.hide()
			}
		}, modifier = Modifier.padding(end = 8.dp)) {
			Icon(
				imageVector = Icons.Filled.Close,
				contentDescription = stringResource(R.string.view_menu_close_button_icon_name),
				tint = MaterialTheme.colors.onSurface,
			)
		}
	}
}