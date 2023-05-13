package com.willbanksy.vulnfind.ui.components

import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
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
import androidx.paging.PagingSource
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemsIndexed
import com.willbanksy.vulnfind.DetailsActivity
import com.willbanksy.vulnfind.models.MainViewModel
import com.willbanksy.vulnfind.R
import com.willbanksy.vulnfind.data.source.local.VulnDBVulnWithMetricsAndReferencesDto
import com.willbanksy.vulnfind.data.source.local.mapToItem
import com.willbanksy.vulnfind.ui.state.ListingFilter

@Composable
fun VulnListView(model: MainViewModel, filter: MutableState<ListingFilter>, additionalPadding: PaddingValues = PaddingValues(0.dp), pagingSourceFactory: (() -> PagingSource<Int, VulnDBVulnWithMetricsAndReferencesDto>)? = null) {
	val pager = remember(filter.value) {
		Pager(
			PagingConfig(
				pageSize = 600,
				enablePlaceholders = true,
				maxSize = MAX_SIZE_UNBOUNDED
			)
		) {
			if(pagingSourceFactory != null) {
				pagingSourceFactory()
			} else {
				model.observeAll(filter.value)
			}
		}
	}
	val lazyPagingItems = pager.flow.collectAsLazyPagingItems()
	
	Box(
		modifier = Modifier
			.fillMaxSize(),
		contentAlignment = Alignment.Center
	) {
		LazyColumn(
			verticalArrangement = Arrangement.spacedBy(8.dp),
			contentPadding = PaddingValues(
				start = 8.dp + additionalPadding.calculateStartPadding(LayoutDirection.Rtl),
				top = 8.dp + additionalPadding.calculateTopPadding(),
				end = 8.dp + additionalPadding.calculateEndPadding(LayoutDirection.Rtl),
				bottom = 8.dp + additionalPadding.calculateBottomPadding()
			),
			modifier = Modifier.fillMaxSize()
		) {
			itemsIndexed(lazyPagingItems) {_, dtoItem ->
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
		
		AnimatedVisibility(
			visible = lazyPagingItems.loadState.refresh == LoadState.Loading || lazyPagingItems.itemCount == 0,
			enter = fadeIn(tween(100)),
			exit = fadeOut(tween(100))
		) {
			if(lazyPagingItems.itemCount == 0) {
				Box(
					modifier = Modifier
						.fillMaxSize()
						.background(MaterialTheme.colors.surface.copy(alpha = 0.4f)),
					contentAlignment = Alignment.Center,
				) {
					if(lazyPagingItems.loadState.refresh == LoadState.Loading) {
						Text(text = stringResource(R.string.view_loading_text))
					} else {
						Text(text = stringResource(R.string.view_none_found_text))
					}
				}
			} else {
				Box(
					modifier = Modifier
						.fillMaxSize()
						.background(MaterialTheme.colors.surface.copy(alpha = 0.0f)), // Specifying background colour or the contents won't show up weirdly
					contentAlignment = Alignment.Center
				) {
					CircularProgressIndicator(
						color = MaterialTheme.colors.primary
					)
				}
			}
		}
	}
}