package com.willbanksy.vulnfind.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.willbanksy.vulnfind.R
import com.willbanksy.vulnfind.models.MainViewModel
import com.willbanksy.vulnfind.ui.state.ListingFilter
import kotlinx.coroutines.launch
import java.time.LocalDateTime

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ListFilterSheetView(model: MainViewModel, bottomSheetState: BottomSheetState, filter: MutableState<ListingFilter>, sheetPeekHeight: MutableState<Dp>) {
//	val firstYearStart = ZonedDateTime.of(1988, 1, 1, 0, 0, 0, 0, ZoneId.ofOffset("UTC", ZoneOffset.UTC))
//	val firstYearEnd = ZonedDateTime.of(1988, 12, 31, 23, 59, 59, 0, ZoneId.ofOffset("UTC", ZoneOffset.UTC))
	
	val bottomPadding = WindowInsets.systemBars.asPaddingValues().calculateBottomPadding()
//	val topPadding = WindowInsets.systemBars.asPaddingValues().calculateTopPadding()
	val fractionToExpanded = if(bottomSheetState.progress.fraction == 0f) {
		if(bottomSheetState.isExpanded) { 1f } else { 0f }
	} else {
		if(bottomSheetState.progress.to == BottomSheetValue.Expanded) {
			bottomSheetState.progress.fraction
		} else {
			1 - bottomSheetState.progress.fraction
		}
	}
	
	val localDensity = LocalDensity.current
	Column(
		modifier = Modifier.background(MaterialTheme.colors.surface.copy(alpha = 1 - fractionToExpanded))
	) {
		Column(
			modifier = Modifier.background(MaterialTheme.colors.surface)//Color.Red.copy(alpha = 0.2f))
		) {
			Row(
				modifier = Modifier
					.fillMaxWidth()
					.onGloballyPositioned { coordinates ->
						sheetPeekHeight.value =
							with(localDensity) { coordinates.size.height.toDp() }
					},
//					.padding(0.dp),
//					.offset(y = topPadding * fractionToExpanded),
				verticalAlignment = Alignment.CenterVertically
			) {
				Spacer(modifier = Modifier.width(16.dp))
				Icon(imageVector = Icons.Filled.FilterAlt, contentDescription = "")
				
				val sb = StringBuilder()
				sb.append(stringResource(id = R.string.view_listing_filters_filters_title))
				if(filter.value.numFilters() != 0) {
					sb.append(" (")
					sb.append(filter.value.numFilters())
					sb.append(")")
				}
				
				Text(
					text = sb.toString(),
					style = MaterialTheme.typography.h6,
					maxLines = 1,
					overflow = TextOverflow.Ellipsis,
					modifier = Modifier.padding(16.dp)
				)
				Spacer(modifier = Modifier.weight(1f))
				
				val coroutineScope = rememberCoroutineScope()
				
				IconButton(onClick = {
					if(bottomSheetState.isCollapsed) {
						coroutineScope.launch {
							bottomSheetState.expand()
						}
					} else {
						coroutineScope.launch {
							bottomSheetState.collapse()
						}
					}
				}, modifier = Modifier.padding(end = 8.dp)) {
					Icon(
						imageVector = Icons.Filled.ExpandLess,
						contentDescription = "",
						modifier = Modifier.rotate(180f * fractionToExpanded)
					)
				}
			}
//			Spacer(modifier = Modifier.height(topPadding))
		}
		
		Column(
			modifier = Modifier.alpha(fractionToExpanded)
		) {
			val dialogVisibility = remember {
				mutableStateOf(false)
			}
			DatePickerDialog(
				showing = dialogVisibility,
				initialYear = filter.value.year,
				initialMonth = filter.value.month,
				yearRange = 1988..LocalDateTime.now().year,
				onDateChange = { year, month ->
					filter.value = filter.value.copy(year = year, month = month)
				}
			)
	
			val monthYearFilterStr: @Composable () -> String = {
				val sb = StringBuilder()
				sb.append(stringResource(id = R.string.view_listing_filters_month_year))
				var filters = false
				if(filter.value.month != null) {
					sb.append(" (")
					sb.append(localisedMonth(month = filter.value.month!!))
					filters = true
				}
				if(filter.value.year != null) {
					if(!filters) {
						sb.append(" (")
					} else {
						sb.append(", ")
					}
					sb.append(filter.value.year.toString())
					filters = true
				}
				if(filters) {
					sb.append(')')
				}
				sb.toString()
			}
			
			HorizontalItemView(
				modifier = Modifier.clickable {
					dialogVisibility.value = true
				},
				icon = Icons.Filled.EditCalendar,
				iconDesc = stringResource(id = R.string.view_listing_filters_month_year_icon),
				itemDesc = monthYearFilterStr()
			)
		}
		
		Spacer(modifier = Modifier.height(bottomPadding))
	}
}