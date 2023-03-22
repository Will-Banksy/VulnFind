package com.willbanksy.vulnfind.ui.components

import androidx.activity.result.ActivityResultLauncher
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.work.*
import com.willbanksy.vulnfind.ui.BottomSheetMode

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BottomSheetView(sheetState: ModalBottomSheetState, sheetMode: MutableState<BottomSheetMode>, notifPermissionRequest: ActivityResultLauncher<String>) {
	when (sheetMode.value) {
		BottomSheetMode.MENU -> {
			SheetModeMenuView(sheetState, sheetMode)
		}
		BottomSheetMode.DOWNLOAD_CONFIRM -> {
			SheetModeDownloadConfirmView(sheetState, sheetMode, notifPermissionRequest)
		}
		BottomSheetMode.HELP -> {
			SheetModeHelpView(sheetState)
		}
	}
}