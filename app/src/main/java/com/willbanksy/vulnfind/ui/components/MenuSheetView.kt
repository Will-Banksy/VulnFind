package com.willbanksy.vulnfind.ui.components

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.willbanksy.vulnfind.model.VulnListModel
import com.willbanksy.vulnfind.R
import com.willbanksy.vulnfind.SettingsActivity
import com.willbanksy.vulnfind.workers.DownloadWorker
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MenuSheetView(model: VulnListModel, sheetState: ModalBottomSheetState, notifPermissionRequest: ActivityResultLauncher<String>) {
	val bottomPadding = WindowInsets.systemBars.asPaddingValues().calculateBottomPadding()
	Column(
		modifier = Modifier.padding(bottom = bottomPadding)
	) {
		Row(
			modifier = Modifier
				.background(MaterialTheme.colors.surface)
				.fillMaxWidth(),
			verticalAlignment = Alignment.CenterVertically
		) {
			Text(
				text = stringResource(R.string.view_menu_title),
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
		
		val context = LocalContext.current
		val coroutineScope = rememberCoroutineScope()
		
		MenuSheetItemView(modifier = Modifier.clickable {
			// TODO: Confirmation sheet
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
				if(context.checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED){
					val work: WorkRequest = OneTimeWorkRequestBuilder<DownloadWorker>().build()
					WorkManager.getInstance(context).enqueue(work)
				} else {
					notifPermissionRequest.launch(Manifest.permission.POST_NOTIFICATIONS)
				}
			}
		},
			icon = Icons.Filled.Refresh,
			iconDesc = stringResource(R.string.view_menu_refresh_icon_name),
			itemDesc = stringResource(R.string.view_menu_refresh_title)
		)
		MenuSheetItemView(modifier = Modifier.clickable {
			coroutineScope.launch {
				sheetState.hide()
			}
			val intent = Intent(context, SettingsActivity::class.java)
			startActivity(context, intent, null)
		},
			icon = Icons.Filled.Settings,
			iconDesc = stringResource(R.string.view_menu_settings_icon_name),
			itemDesc = stringResource(R.string.view_menu_settings_title)
		)
		MenuSheetItemView(modifier = Modifier.clickable {
			// TODO
		},
			icon = Icons.Filled.Info,
			iconDesc = stringResource(R.string.view_menu_about_icon_name),
			itemDesc = stringResource(R.string.view_menu_about_title)
		)
	}
}