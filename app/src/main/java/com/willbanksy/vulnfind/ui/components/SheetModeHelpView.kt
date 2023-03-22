package com.willbanksy.vulnfind.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.willbanksy.vulnfind.R

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SheetModeHelpView(sheetState: ModalBottomSheetState) {
	val bottomPadding = WindowInsets.systemBars.asPaddingValues().calculateBottomPadding()
	Column(
		modifier = Modifier.padding(bottom = bottomPadding)
	) {
		MenuSheetHeaderView(sheetState, headerText = stringResource(R.string.view_menu_help_header))
		Column(
			modifier = Modifier.padding(16.dp)
		) {
			Text(text = stringResource(R.string.view_menu_help_main_text))
		}
	}
}