package com.willbanksy.vulnfind.ui.components

import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.willbanksy.vulnfind.R
import com.willbanksy.vulnfind.SearchActivity
import com.willbanksy.vulnfind.ui.BottomSheetMode
import com.willbanksy.vulnfind.workers.DownloadWorker
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DefaultScaffoldView(
	topBarLabel: String,
	topBarShowBack: Boolean = false,
	showBottomBar: Boolean = false,
	bottomSheetState: ModalBottomSheetState? = null,
	bottomSheetMode: MutableState<BottomSheetMode>? = null,
	content: @Composable () -> Unit
) {
	val coroutineScope = rememberCoroutineScope()
	val scaffoldState = rememberScaffoldState()
	val context = LocalContext.current
	val bottomPadding = WindowInsets.systemBars.asPaddingValues().calculateBottomPadding()
	
	val workInfoLiveData = WorkManager.getInstance(LocalContext.current).getWorkInfosForUniqueWorkLiveData(
		DownloadWorker.UNIQUE_WORK_ID)
	val workerState = remember {
		mutableStateOf(WorkInfo.State.ENQUEUED) // Just using ENQUEUED as a default state, don't actually care whether it is enqueued
	}
	workInfoLiveData.observe(LocalLifecycleOwner.current) { workInfos ->
		val workInfo = workInfos.getOrNull(0)
		val newWorkerState = workInfo?.state ?: WorkInfo.State.ENQUEUED
		if(workerState.value == WorkInfo.State.RUNNING && newWorkerState == WorkInfo.State.SUCCEEDED) {
			coroutineScope.launch {
				scaffoldState.snackbarHostState.showSnackbar(context.getString(R.string.view_download_successful))
			}
		} else if(workerState.value == WorkInfo.State.RUNNING && newWorkerState == WorkInfo.State.FAILED) {
			coroutineScope.launch {
				scaffoldState.snackbarHostState.showSnackbar(context.getString(R.string.view_download_failed))
			}
		}
		workerState.value = newWorkerState
	}

	Scaffold(
		scaffoldState = scaffoldState,
		snackbarHost = {
			SnackbarHost(scaffoldState.snackbarHostState) { data ->
				val snackbarOffsetY = if(!showBottomBar) {
					-bottomPadding
				} else {
					0.dp
				}
				Snackbar(
					modifier = Modifier.offset(y = snackbarOffsetY),
					snackbarData = data,
					backgroundColor = MaterialTheme.colors.surface,
					contentColor = MaterialTheme.colors.onSurface,
					elevation = 4.dp
				)
			}
		},
		topBar = {
			TopBarView(label = topBarLabel, showBackButton = topBarShowBack)
		},
		bottomBar = {
			if(showBottomBar) {
				BottomAppBar(
					contentPadding = PaddingValues(bottom = bottomPadding, start = 8.dp, end = 8.dp),
					elevation = 0.dp,
					backgroundColor = MaterialTheme.colors.surface
				) {
					IconButton(onClick = {
						bottomSheetMode?.value = BottomSheetMode.MENU
						coroutineScope.launch {
							bottomSheetState?.show()
						}
					}) {
						Icon(
							imageVector = Icons.Filled.Menu,
							contentDescription = stringResource(R.string.view_menu_button),
							tint = MaterialTheme.colors.onSurface
						)
					}
					Spacer(modifier = Modifier.weight(1f))
					val searchIntent = Intent(context, SearchActivity::class.java)
					IconButton(onClick = {
						startActivity(context, searchIntent, null)
					}) {
						Icon(
							imageVector = Icons.Filled.Search,
							contentDescription = stringResource(R.string.view_search_button),
							tint = MaterialTheme.colors.onSurface
						)
					}
				}
			}
		}
	) { scaffoldPadding ->
		Box(
			modifier = Modifier.fillMaxSize().padding(scaffoldPadding)
		) {
			content()
		}
	}
}