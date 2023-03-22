package com.willbanksy.vulnfind.ui

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.willbanksy.vulnfind.R
import com.willbanksy.vulnfind.models.MainViewModel
import com.willbanksy.vulnfind.ui.components.BottomSheetView
import com.willbanksy.vulnfind.ui.components.HomeScreenView
import com.willbanksy.vulnfind.ui.components.TopBarView
import kotlinx.coroutines.launch

enum class BottomSheetMode {
	MENU,
	DOWNLOAD_CONFIRM,
	HELP
}

@OptIn(ExperimentalMaterialApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MainActivityView(model: MainViewModel, notifPermissionRequest: ActivityResultLauncher<String>) {
	Surface(
		modifier = Modifier.fillMaxSize(),
		color = MaterialTheme.colors.background
	) {
		val bottomSheetMode = remember {
			mutableStateOf(BottomSheetMode.MENU)
		}
		val bottomSheetState = rememberModalBottomSheetState(
			initialValue = ModalBottomSheetValue.Hidden,
			animationSpec = tween(350),
		)
		ModalBottomSheetLayout(
			sheetState = bottomSheetState,
			scrimColor = Color.Black.copy(alpha = 0.8f),
			sheetBackgroundColor = MaterialTheme.colors.surface,
			sheetElevation = 2.dp,
			sheetContent = {
				BottomSheetView(bottomSheetState, bottomSheetMode, notifPermissionRequest)
			}
		) {
			val coroutineScope = rememberCoroutineScope()
			BackHandler(
				enabled = bottomSheetState.isVisible
			) {
				coroutineScope.launch {
					bottomSheetState.hide()
				}
			}
			Scaffold(
				topBar = {
					TopBarView(label = stringResource(R.string.activity_main_title))
				},
				bottomBar = {
					val bottomPadding = WindowInsets.systemBars.asPaddingValues().calculateBottomPadding()
					BottomAppBar(
						contentPadding = PaddingValues(bottom = bottomPadding, start = 8.dp, end = 8.dp),
						elevation = 0.dp,
						backgroundColor = MaterialTheme.colors.surface
					) {
						IconButton(onClick = {
							bottomSheetMode.value = BottomSheetMode.MENU
							coroutineScope.launch {
								bottomSheetState.show()
							}
						}) {
							Icon(
								imageVector = Icons.Filled.Menu,
								contentDescription = stringResource(R.string.view_menu_button),
								tint = MaterialTheme.colors.onSurface
							)
						}
						Spacer(modifier = Modifier.weight(1f))
						IconButton(onClick = { /*TODO*/ }) {
							Icon(
								imageVector = Icons.Filled.Search,
								contentDescription = stringResource(R.string.view_search_button),
								tint = MaterialTheme.colors.onSurface
							)
						}
					}
				}
			) {
				HomeScreenView(model, sheetState = bottomSheetState, sheetMode = bottomSheetMode)
			}
		}
	}
}