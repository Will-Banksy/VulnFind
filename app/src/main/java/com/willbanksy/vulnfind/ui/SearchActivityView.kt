package com.willbanksy.vulnfind.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.willbanksy.vulnfind.R
import com.willbanksy.vulnfind.models.MainViewModel
import com.willbanksy.vulnfind.ui.components.DefaultScaffoldView
import com.willbanksy.vulnfind.ui.content_views.VulnListView
import com.willbanksy.vulnfind.ui.state.ListingFilter

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SearchActivityView(model: MainViewModel) {
	DefaultScaffoldView(topBarLabel = stringResource(id = R.string.activity_search_title), topBarShowBack = true) {
		Column {
			val searchEntry = remember {
				mutableStateOf("")
			}
			TextField(value = searchEntry.value, singleLine = true, modifier = Modifier
				.fillMaxWidth()
				.padding(8.dp),
				leadingIcon = {
					Icon(imageVector = Icons.Filled.Search, contentDescription = stringResource(
					  id = R.string.view_search_search_icon
					))
				},
				onValueChange = { entered ->
					searchEntry.value = entered
				}
			)
			val filter = remember {
				mutableStateOf(ListingFilter())
			}
			val bottomPadding = WindowInsets.systemBars.asPaddingValues().calculateBottomPadding()
			VulnListView(model = model, filter = filter, additionalPadding = PaddingValues(bottom = bottomPadding), pagerFactory = {
				remember(searchEntry.value) {
					Pager(
						PagingConfig(
							pageSize = 600,
							enablePlaceholders = true,
							maxSize = PagingConfig.MAX_SIZE_UNBOUNDED
						)
					) {
						model.observeAllFilteredById(searchEntry.value)
					}
				}
			})
		}
	}
}