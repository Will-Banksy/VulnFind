package com.willbanksy.vulnfind.ui.content_views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Scaffold
import androidx.compose.material.Snackbar
import androidx.compose.material.SnackbarHost
import androidx.compose.material.Text
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.willbanksy.vulnfind.R
import com.willbanksy.vulnfind.models.MainViewModel
import com.willbanksy.vulnfind.ui.components.HorizontalItemView
import com.willbanksy.vulnfind.ui.components.SettingsBottomSheetView
import com.willbanksy.vulnfind.ui.state.SettingsSheetMode
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SettingsView(model: MainViewModel) {
	val settingsSheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
	val settingsSheetMode = remember {
		mutableStateOf(SettingsSheetMode.API_KEY)
	}
	val sheetControlFocusRequester = remember {
		FocusRequester()
	}
	val scaffoldState = rememberScaffoldState()	
	
	Scaffold(
		scaffoldState = scaffoldState,
		snackbarHost = { snackbarHostState ->
			SnackbarHost(hostState = snackbarHostState) { snackbarData ->
				val bottomPadding = WindowInsets.systemBars.asPaddingValues().calculateBottomPadding()
				Snackbar(
					modifier = Modifier.offset(y = -bottomPadding),
					snackbarData = snackbarData,
					backgroundColor = MaterialTheme.colors.surface,
					contentColor = MaterialTheme.colors.onSurface,
					elevation = 4.dp
				)
			}
		}
	) { scaffoldPadding ->
		ModalBottomSheetLayout(
			modifier = Modifier.padding(scaffoldPadding),
			sheetState = settingsSheetState,
			scrimColor = Color.Black.copy(alpha = 0.8f),
			sheetElevation = 2.dp,
			sheetContent = {
				SettingsBottomSheetView(model = model, sheetState = settingsSheetState, sheetMode = settingsSheetMode, scaffoldState.snackbarHostState, sheetControlFocusRequester)
			},
		) {
			Column(
				modifier = Modifier.padding(top = 16.dp)
			) {
				val coroutineScope = rememberCoroutineScope()
				
				SettingsSectionTextView(text = stringResource(R.string.view_settings_section_api_key_title)) // Section: API Key
				SettingsItemView( // Item: API Key
					modifier = Modifier.clickable {
						settingsSheetMode.value = SettingsSheetMode.API_KEY
						coroutineScope.launch {
							settingsSheetState.show()
						}
						sheetControlFocusRequester.requestFocus()
					},
					title = stringResource(R.string.view_settings_item_api_key_title),
					description = stringResource(R.string.view_settings_item_api_key_description)
				)
				
				SettingsSectionDivider()
				
				SettingsSectionTextView(text = stringResource(R.string.view_settings_section_data_usage_title)) // Section: Data Usage
				SettingsItemView( // Item: Metered connections
					modifier = Modifier.clickable { /* TODO */ },
					title = stringResource(R.string.view_settings_item_metered_connections_title),
					description = stringResource(R.string.view_settings_item_metered_connections_description)
				)
			}
		}
	}
}

@Composable
fun SettingsSectionTextView(text: String) {
	Text(
		text = text,
		modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
		style = MaterialTheme.typography.body2,
		color = MaterialTheme.colors.primary
	)
}

@Composable
fun SettingsSectionDivider() {
	Spacer(modifier = Modifier.height(8.dp))
	Divider()
	Spacer(modifier = Modifier.height(8.dp))
}

@Composable
fun SettingsItemView(modifier: Modifier, title: String, description: String) {
	HorizontalItemView(
		modifier = modifier
	) {
		Column {
			Text(text = title)
			Text(
				text = description,
				style = MaterialTheme.typography.body2,
				color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
			)
		}
	}
}