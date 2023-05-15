package com.willbanksy.vulnfind.ui.components

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Key
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.willbanksy.vulnfind.R
import com.willbanksy.vulnfind.data.SettingsData
import com.willbanksy.vulnfind.models.MainViewModel
import com.willbanksy.vulnfind.ui.state.SettingsSheetMode
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SettingsBottomSheetView(model: MainViewModel, sheetState: ModalBottomSheetState, sheetMode: MutableState<SettingsSheetMode>, snackbarHostState: SnackbarHostState, focusRequester: FocusRequester? = null) {
	when(sheetMode.value) {
		SettingsSheetMode.API_KEY -> {
			SettingsSheetModeApiKeyView(model, sheetState, snackbarHostState, focusRequester)
		}
	}
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SettingsSheetModeApiKeyView(model: MainViewModel, sheetState: ModalBottomSheetState, snackbarHostState: SnackbarHostState, focusRequester: FocusRequester?) {
	val topPadding = WindowInsets.systemBars.asPaddingValues().calculateTopPadding()
	val scrollState = rememberScrollState()
	BoxWithConstraints {
		val maxHeight = maxHeight
		Box(
			modifier = Modifier.heightIn(max = maxHeight - topPadding)
		) {
			Column(
				modifier = Modifier
					.navigationBarsPadding()
					.imePadding()
					.verticalScroll(scrollState)
			) {
				ModalSheetHeaderView(sheetState = sheetState, headerText = stringResource(id = R.string.view_settings_sheet_enter_api_key))
				val apiKey = model.observeSettings().collectAsState(initial = SettingsData())
				val currApiKey = remember(apiKey.value) {
					mutableStateOf(apiKey.value.apiKey ?: "")
				}
				HorizontalItemView {
					if(!sheetState.isVisible || sheetState.targetValue == ModalBottomSheetValue.Hidden) {
						LocalFocusManager.current.clearFocus()
					}
					TextField(value = currApiKey.value, modifier = Modifier
						.focusRequester(focusRequester ?: FocusRequester())
						.fillMaxWidth(),
						leadingIcon = {
							Icon(imageVector = Icons.Filled.Key, contentDescription = stringResource(id = R.string.view_settings_sheet_api_key_icon))
						},
						onValueChange = { entered ->
							currApiKey.value = entered
						},
						singleLine = true
					)
				}
				Row(
					modifier = Modifier.padding(horizontal = 16.dp),
					verticalAlignment = Alignment.CenterVertically
				) {
					val coroutineScope = rememberCoroutineScope()
					val snackbarMsg = stringResource(id = R.string.view_settings_snackbar_message_api_key_updated)
					
					Text(text = stringResource(id = R.string.view_settings_sheet_api_key_warning), modifier = Modifier.weight(1f))
					Spacer(modifier = Modifier.height(16.dp))
					Button(onClick = {
						coroutineScope.launch {
							Log.d("Update API Key", currApiKey.value)
							model.updateSettingApiKey(currApiKey.value)
						}
						coroutineScope.launch {
							sheetState.hide()
						}
						coroutineScope.launch {
							snackbarHostState.showSnackbar(snackbarMsg)
						}
					}) {
						Text(text = stringResource(id = R.string.view_apply_changes).uppercase())
					}
				}
				Spacer(modifier = Modifier.height(16.dp))
			}
		}
	}
}