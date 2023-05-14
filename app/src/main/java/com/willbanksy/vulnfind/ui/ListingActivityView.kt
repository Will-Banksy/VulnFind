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
import com.willbanksy.vulnfind.ui.content_views.VulnListView
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
		
		val sheetPeekHeight = remember {
			mutableStateOf(0.dp)
		}
		
		BottomSheetScaffold(
			scaffoldState = scaffoldState,
			sheetPeekHeight = sheetPeekHeight.value + bottomPadding,
			sheetElevation = 2.dp,
			topBar = {
				TopBarView(label = stringResource(R.string.activity_listing_title), true)
			},
			sheetContent = {
				ListFilterSheetView(scaffoldState.bottomSheetState, filter, sheetPeekHeight)
			},
		) { padding ->
			val coroutineScope = rememberCoroutineScope()
			BackHandler(scaffoldState.bottomSheetState.isExpanded) {
				coroutineScope.launch {
					scaffoldState.bottomSheetState.collapse()
				}
			}
			VulnListView(model = model, filter = filter, additionalPadding = padding)
		}
	}
}