package com.willbanksy.vulnfind.ui.components

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.willbanksy.vulnfind.R
import com.willbanksy.vulnfind.ui.BottomSheetMode
import com.willbanksy.vulnfind.workers.DownloadWorker

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SheetModeDownloadConfirmView(sheetState: ModalBottomSheetState, sheetMode: MutableState<BottomSheetMode>, notifPermissionRequest: ActivityResultLauncher<String>) {
	val bottomPadding = WindowInsets.systemBars.asPaddingValues().calculateBottomPadding()
	Column(
		modifier = Modifier.padding(bottom = bottomPadding)
	) {
		MenuSheetHeaderView(sheetState, headerText = stringResource(R.string.view_menu_download_confirm_header))
		Column(
			modifier = Modifier.padding(16.dp)
		) {
			Text(text = stringResource(R.string.view_menu_download_confirm_main_text))
			Spacer(modifier = Modifier.height(16.dp))
			Row {
				Spacer(modifier = Modifier.weight(1f))
				val context = LocalContext.current
				Button(onClick = {
					if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
						if(context.checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED){
							val work: OneTimeWorkRequest = OneTimeWorkRequestBuilder<DownloadWorker>().build()
							WorkManager.getInstance(context).enqueueUniqueWork(
								DownloadWorker.UNIQUE_WORK_ID,
								ExistingWorkPolicy.KEEP,
								work
							)
							sheetMode.value = BottomSheetMode.MENU
						} else {
							notifPermissionRequest.launch(Manifest.permission.POST_NOTIFICATIONS)
						}
					}
				}) {
					Text(text = stringResource(R.string.view_menu_download_confirm_button_text).uppercase())
				}
			}
		}
	}
}