package com.willbanksy.vulnfind.ui.components

import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import androidx.paging.LoadState
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingConfig.Companion.MAX_SIZE_UNBOUNDED
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemsIndexed
import com.willbanksy.vulnfind.DetailsActivity
import com.willbanksy.vulnfind.models.MainViewModel
import com.willbanksy.vulnfind.R
import com.willbanksy.vulnfind.data.source.local.mapToItem

@Composable
fun VulnListView(model: MainViewModel, additionalPadding: PaddingValues = PaddingValues(0.dp)) {
//	val vulns = model.vulnsStream.collectAsState(initial = emptyList())
	val pager = remember {
		Pager(
			PagingConfig(
				pageSize = 600,
				enablePlaceholders = true,
				maxSize = MAX_SIZE_UNBOUNDED
			)
		) {
			model.vulnsStream
		}
	}
	val lazyPagingItems = pager.flow/*.map { pagingData ->
		pagingData.map { dto ->
			mapToItem(dto)
		}
	}*/.collectAsLazyPagingItems()

	Box(
		modifier = Modifier
			.fillMaxSize()
	) {
		LazyColumn(
			verticalArrangement = Arrangement.spacedBy(8.dp),
			contentPadding = PaddingValues(
				start = 8.dp + additionalPadding.calculateStartPadding(LayoutDirection.Rtl),
				top = 8.dp + additionalPadding.calculateTopPadding(),
				end = 8.dp + additionalPadding.calculateEndPadding(LayoutDirection.Rtl),
				bottom = 8.dp + additionalPadding.calculateBottomPadding()
			)
		) {
			if(lazyPagingItems.loadState.refresh == LoadState.Loading) {
				item {
					Box(
						modifier = Modifier.fillMaxSize(),
						contentAlignment = Alignment.Center
					) {
						Text(
							text = stringResource(R.string.view_loading_text),
							color = MaterialTheme.colors.onSurface.copy(alpha = 0.5f)
						)
					}
				}
			}
			
			itemsIndexed(lazyPagingItems) {index, dtoItem ->
				val item = dtoItem.let { dto ->
					if(dto == null) {
						return@let null
					} else {
						return@let mapToItem(dto)
					}
				}
				if(item != null) {
					val intent = Intent(LocalContext.current, DetailsActivity::class.java).apply {
						putExtra("cveId", item.cveId)
					}
					val context = LocalContext.current
					VulnItemCardView(
						modifier = Modifier.clickable {
							startActivity(context, intent, null)
						},
						vuln = item
					)
				}
			}
		}
	}
}