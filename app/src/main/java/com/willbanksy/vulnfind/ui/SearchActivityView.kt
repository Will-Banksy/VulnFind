package com.willbanksy.vulnfind.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.willbanksy.vulnfind.R
import com.willbanksy.vulnfind.models.MainViewModel
import com.willbanksy.vulnfind.ui.components.DefaultScaffoldView

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SearchActivityView(model: MainViewModel) {
	DefaultScaffoldView(topBarLabel = stringResource(id = R.string.activity_search_title)) {
		Column {
			val searchEntry = remember {
				mutableStateOf("")
			}
			TextField(value = searchEntry.value, modifier = Modifier.fillMaxWidth().padding(8.dp), onValueChange = { entered ->
				searchEntry.value = entered
			})
		}
	}
}