package com.willbanksy.vulnfind.ui

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.willbanksy.vulnfind.R
import com.willbanksy.vulnfind.models.MainViewModel
import com.willbanksy.vulnfind.ui.components.ListFilterSheetView
import com.willbanksy.vulnfind.ui.components.TopBarView
import com.willbanksy.vulnfind.ui.components.VulnListView
import com.willbanksy.vulnfind.ui.state.ListingFilter
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ListingActivityView(model: MainViewModel) {
	Surface(
		modifier = Modifier.fillMaxSize(),
		color = MaterialTheme.colors.background
	) {
		val scaffoldState = rememberBottomSheetScaffoldState()
		
		val filter = remember {
			mutableStateOf(ListingFilter())
		}
		val bottomPadding = WindowInsets.systemBars.asPaddingValues().calculateBottomPadding()
		
		BottomSheetScaffold(
			scaffoldState = scaffoldState,
			sheetPeekHeight = 64.dp + bottomPadding,
			sheetElevation = 2.dp,
			topBar = {
				TopBarView(label = stringResource(R.string.activity_listing_title), true)
			},
			sheetContent = {
				ListFilterSheetView(model = model, scaffoldState.bottomSheetState, filter)
			},
		) {
			val coroutineScope = rememberCoroutineScope()
			BackHandler(scaffoldState.bottomSheetState.isExpanded) {
				coroutineScope.launch {
					scaffoldState.bottomSheetState.collapse()
				}
			}
			VulnListView(model = model, filter = filter)
		}
//		DefaultScaffoldView(topBarLabel = stringResource(R.string.activity_listing_title), topBarShowBack = true) {
//			//VulnListView(model, PaddingValues(bottom = bottomPadding))
//			ListingDateView(model = model)
//		}
	}
}