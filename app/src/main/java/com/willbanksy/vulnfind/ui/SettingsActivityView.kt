package com.willbanksy.vulnfind.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.willbanksy.vulnfind.R
import com.willbanksy.vulnfind.model.VulnListModel
import com.willbanksy.vulnfind.ui.components.SettingsItemView
import com.willbanksy.vulnfind.ui.components.TopBarView

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun SettingsActivityView(model: VulnListModel) {
	Surface(
		modifier = Modifier.fillMaxSize(),
		color = MaterialTheme.colors.background
	) {
		Scaffold(
			topBar = {
				TopBarView(label = stringResource(R.string.activity_settings_title), true)
			}
		) {
			Column(
				verticalArrangement = Arrangement.spacedBy(4.dp),
				horizontalAlignment = Alignment.Start
			) {
				Spacer(modifier = Modifier.height(8.dp))
				Text( // Section: API Key
					text = stringResource(R.string.view_settings_section_api_key_title),
					modifier = Modifier.padding(horizontal = 16.dp),
					style = MaterialTheme.typography.body2,
					color = MaterialTheme.colors.primary
				)
				SettingsItemView( // Item: API Key
					modifier = Modifier.clickable { /* TODO */ },
					title = stringResource(R.string.view_settings_item_api_key_title),
					description = stringResource(R.string.view_settings_item_api_key_description)
				)
				Spacer(modifier = Modifier.height(8.dp))
				Text( // Section: Data Usage
					text = stringResource(R.string.view_settings_section_data_usage_title),
					modifier = Modifier.padding(horizontal = 16.dp),
					style = MaterialTheme.typography.body2,
					color = MaterialTheme.colors.primary
				)
				SettingsItemView( // Item: Metered connections
					modifier = Modifier.clickable { /* TODO */ },
					title = stringResource(R.string.view_settings_item_metered_connections_title),
					description = stringResource(R.string.view_settings_item_metered_connections_description)
				)
//				TextField(
//					modifier = Modifier.fillMaxWidth(),
//					value = model.apiKey,
//					onValueChange = { newStr: String ->
//						model.apiKey = newStr
//					},
//					label = {
//						Text(
//							text = stringResource(R.string.view_settings_api_key_label),
//							style = MaterialTheme.typography.caption
//						)
//					},
//				)
			}
		}
	}
}