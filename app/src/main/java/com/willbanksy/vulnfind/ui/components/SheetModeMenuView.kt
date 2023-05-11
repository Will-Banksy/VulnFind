package com.willbanksy.vulnfind.ui.components

import android.content.Intent
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import androidx.work.*
import com.willbanksy.vulnfind.AboutActivity
import com.willbanksy.vulnfind.R
import com.willbanksy.vulnfind.SettingsActivity
import com.willbanksy.vulnfind.ui.BottomSheetMode
import com.willbanksy.vulnfind.workers.DownloadWorker
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SheetModeMenuView(sheetState: ModalBottomSheetState, sheetMode: MutableState<BottomSheetMode>) {
	val bottomPadding = WindowInsets.systemBars.asPaddingValues().calculateBottomPadding()
	Column(
		modifier = Modifier.padding(bottom = bottomPadding)
	) {
		MenuSheetHeaderView(sheetState, headerText = stringResource(R.string.view_menu_title))
		
		val context = LocalContext.current
		val coroutineScope = rememberCoroutineScope()
		
		val workInfoLiveData = WorkManager.getInstance(context).getWorkInfosForUniqueWorkLiveData(DownloadWorker.UNIQUE_WORK_ID)
		val workerProgress = remember {
			mutableStateOf(-1f)
		}
		workInfoLiveData.observe(LocalLifecycleOwner.current) { workInfos ->
			val workInfo = workInfos.getOrNull(0)
			if(workInfo == null || workInfo.state == WorkInfo.State.CANCELLED) {
				workerProgress.value = -1f
			} else if(workInfo.state == WorkInfo.State.SUCCEEDED) {
				Log.d("DownloadWorker Monitor", "SUCCEEDED")
				workerProgress.value = 100f
			} else {
				workerProgress.value = workInfo.progress.getFloat(DownloadWorker.PARAM_PROGRESS, -1f)
			}
		}
		
		val workerRunning = workerProgress.value != -1f
		
		if(workerRunning) {
			Row(
				modifier = Modifier
					.padding(horizontal = 16.dp)
					.fillMaxWidth(),
				verticalAlignment = Alignment.CenterVertically
			) {
				Icon(
					imageVector = Icons.Filled.Refresh,
					contentDescription = stringResource(R.string.view_menu_refresh_icon_name),
					tint = MaterialTheme.colors.onSurface,
					modifier = Modifier.padding(vertical = 16.dp)
				)
				Spacer(modifier = Modifier.width(16.dp))
				Column(
					modifier = Modifier
						.weight(1f)
				) {
					val txt = stringResource(R.string.view_menu_downloading)
					Text(text = "$txt ${"%.2f".format(workerProgress.value)}%")
					Spacer(modifier = Modifier.height(8.dp))
					LinearProgressIndicator(
						progress = workerProgress.value / 100f,
						modifier = Modifier.fillMaxWidth(),
						color = MaterialTheme.colors.primary
					)
				}
			}
		} else {
			HorizontalItemView(modifier = Modifier.clickable {
				sheetMode.value = BottomSheetMode.DOWNLOAD_CONFIRM
			},
				icon = Icons.Filled.Refresh,
				iconDesc = stringResource(R.string.view_menu_refresh_icon_name),
				itemDesc = stringResource(R.string.view_menu_refresh_title)
			)
		}
		HorizontalItemView(modifier = Modifier.clickable {
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
		HorizontalItemView(modifier = Modifier.clickable {
			sheetMode.value = BottomSheetMode.HELP
		},
			icon = Icons.Filled.Help,
			iconDesc = stringResource(R.string.view_menu_help_icon_name),
			itemDesc = stringResource(R.string.view_menu_help_title)
		)
		HorizontalItemView(modifier = Modifier.clickable {
			coroutineScope.launch {
				sheetState.hide()
			}
			val intent = Intent(context, AboutActivity::class.java)
			startActivity(context, intent, null)
		},
			icon = Icons.Filled.Info,
			iconDesc = stringResource(R.string.view_menu_about_icon_name),
			itemDesc = stringResource(R.string.view_menu_about_title)
		)
	}
}