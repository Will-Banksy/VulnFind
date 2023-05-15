package com.willbanksy.vulnfind.ui.content_views

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.material.Text
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.willbanksy.vulnfind.R
import com.willbanksy.vulnfind.data.SettingsData
import com.willbanksy.vulnfind.models.MainViewModel
import com.willbanksy.vulnfind.ui.components.MainScaffoldView
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
	
	ModalBottomSheetLayout(
		modifier = Modifier,
		sheetState = settingsSheetState,
		scrimColor = Color.Black.copy(alpha = 0.8f),
		sheetElevation = 2.dp,
		sheetContent = {
			SettingsBottomSheetView(model = model, sheetState = settingsSheetState, sheetMode = settingsSheetMode, scaffoldState.snackbarHostState, sheetControlFocusRequester)
		},
	) {
		val coroutineScope = rememberCoroutineScope()
		BackHandler(enabled = settingsSheetState.isVisible, onBack = {
			coroutineScope.launch {
				settingsSheetState.hide()
			}
		})
		
		MainScaffoldView(topBarLabel = stringResource(R.string.activity_settings_title), topBarShowBack = true, scaffoldState = scaffoldState) {
			Column(
				modifier = Modifier
					.padding(top = 16.dp)
			) {
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
				
				val useMeteredConnections = model.observeSettings().collectAsState(initial = SettingsData())
				SettingsItemSwitchView( // Item: Metered connections
					modifier = Modifier.clickable {
						coroutineScope.launch {
							model.updateSettingUseMetered(!useMeteredConnections.value.useMetered)
						}
					},
					title = stringResource(R.string.view_settings_item_metered_connections_title),
					description = stringResource(R.string.view_settings_item_metered_connections_description),
					switchValue = useMeteredConnections.value.useMetered
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

@Composable
fun SettingsItemSwitchView(modifier: Modifier, title: String, description: String, switchValue: Boolean) {
	HorizontalItemView(
		modifier = modifier
	) {
		Column(modifier = Modifier.weight(1f)) {
			Text(text = title)
			Text(
				text = description,
				style = MaterialTheme.typography.body2,
				color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
			)
		}
		Spacer(modifier = Modifier.width(8.dp))
		Switch(
			checked = switchValue,
			onCheckedChange = null,
			colors = SwitchDefaults.colors(checkedThumbColor = MaterialTheme.colors.primary, uncheckedThumbColor = MaterialTheme.colors.onSurface)
		)
	}
}