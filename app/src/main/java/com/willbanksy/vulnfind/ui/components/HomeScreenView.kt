package com.willbanksy.vulnfind.ui.components

import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.List
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.compose.collectAsLazyPagingItems
import com.willbanksy.vulnfind.ListingActivity
import com.willbanksy.vulnfind.R
import com.willbanksy.vulnfind.models.MainViewModel
import com.willbanksy.vulnfind.ui.BottomSheetMode
import com.willbanksy.vulnfind.ui.state.ListingFilter
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeScreenView(model: MainViewModel, sheetState: ModalBottomSheetState, sheetMode: MutableState<BottomSheetMode>) {
	Column {
		val pager = remember {
			Pager(
				PagingConfig(
					pageSize = 600,
					enablePlaceholders = true,
					maxSize = PagingConfig.MAX_SIZE_UNBOUNDED
				)
			) {
				model.observeAll()
			}
		}
		val lazyPagingItems = pager.flow.collectAsLazyPagingItems()
		
		Text(
			text = "${stringResource(R.string.view_home_downloaded_count_text)}: ${lazyPagingItems.itemCount}",
			style = MaterialTheme.typography.caption,
			color = MaterialTheme.colors.onSurface.copy(alpha = 0.5f),
			textAlign = TextAlign.Center,
			modifier = Modifier
				.fillMaxWidth()
				.padding(vertical = 8.dp)
		)
		val context = LocalContext.current
		val intent = Intent(context, ListingActivity::class.java)
		Row(
			modifier = Modifier
				.fillMaxWidth()
				.clickable {
					startActivity(context, intent, null)
				}
		) {
			Row(
				modifier =  Modifier.padding(16.dp)
			) {
				Icon(
					imageVector = Icons.Filled.List,
					contentDescription = stringResource(R.string.view_home_listing_icon_name),
					tint = MaterialTheme.colors.onSurface
				)
				Spacer(modifier = Modifier.width(16.dp))
				Text(text = stringResource(R.string.view_home_listing_title))
			}
		}
		val coroutineScope = rememberCoroutineScope()
		Row(
			modifier = Modifier
				.fillMaxWidth()
				.clickable {
					sheetMode.value = BottomSheetMode.HELP
					coroutineScope.launch {
						sheetState.show()
					}
				}
		) {
			Row(
				modifier =  Modifier.padding(16.dp)
			) {
				Icon(
					imageVector = Icons.Filled.Help,
					contentDescription = stringResource(R.string.view_menu_help_icon_name),
					tint = MaterialTheme.colors.onSurface
				)
				Spacer(modifier = Modifier.width(16.dp))
				Text(text = stringResource(R.string.view_menu_help_title))
			}
		}
		Text(
			text = stringResource(id = R.string.view_home_heading_bookmarked),
			style = MaterialTheme.typography.h6,
			modifier = Modifier.padding(top = 16.dp, bottom = 8.dp).padding(horizontal = 16.dp)
		)
		val filter = remember {
			mutableStateOf(ListingFilter())
		}
		VulnListView(model = model, filter = filter, pagingSourceFactory = {
			model.observeBookmarked()
		})
	}
}