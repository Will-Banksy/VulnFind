package com.willbanksy.vulnfind.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.willbanksy.vulnfind.R
import com.willbanksy.vulnfind.models.MainViewModel
import com.willbanksy.vulnfind.ui.state.ListingFilter
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ListFilterSheetView(model: MainViewModel, bottomSheetState: BottomSheetState, filter: MutableState<ListingFilter>, sheetPeekHeight: MutableState<Dp>) {
//	val firstYearStart = ZonedDateTime.of(1988, 1, 1, 0, 0, 0, 0, ZoneId.ofOffset("UTC", ZoneOffset.UTC))
//	val firstYearEnd = ZonedDateTime.of(1988, 12, 31, 23, 59, 59, 0, ZoneId.ofOffset("UTC", ZoneOffset.UTC))
	
	val bottomPadding = WindowInsets.systemBars.asPaddingValues().calculateBottomPadding()
	val topPadding = WindowInsets.systemBars.asPaddingValues().calculateTopPadding()
	val fractionToExpanded = if(bottomSheetState.progress.fraction == 0f) {
		if(bottomSheetState.isExpanded) { 1f } else { 0f }
	} else {
		if(bottomSheetState.progress.to == BottomSheetValue.Expanded) {
			bottomSheetState.progress.fraction
		} else {
			1 - bottomSheetState.progress.fraction
		}
	}
	
	BoxWithConstraints {
		val localDensity = LocalDensity.current
		Column(
			modifier = Modifier
				.background(MaterialTheme.colors.surface.copy(alpha = 1 - fractionToExpanded))
				.heightIn(max = maxHeight - topPadding)
		) {
			Column(
				modifier = Modifier.background(MaterialTheme.colors.surface)
			) {
				Row(
					modifier = Modifier
						.fillMaxWidth()
						.onGloballyPositioned { coordinates ->
							sheetPeekHeight.value =
								with(localDensity) { coordinates.size.height.toDp() }
						},
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
			}
			
			val scrollState = rememberScrollState()
			
			Column(
				modifier = Modifier.alpha(fractionToExpanded).verticalScroll(scrollState)
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
						if(bottomSheetState.isExpanded) {
							dialogVisibility.value = true
						}
					},
					icon = Icons.Filled.EditCalendar,
					iconDesc = stringResource(id = R.string.view_listing_filters_month_year_icon),
					itemDesc = monthYearFilterStr()
				)
				
				HorizontalItemView {
					Column {
						Row {
							Icon(imageVector = Icons.Filled.Score, contentDescription = stringResource(
								id = R.string.view_listing_filters_score_icon
							))
							Spacer(modifier = Modifier.width(16.dp))
							Text(
								text = stringResource(id = R.string.view_listing_filters_score)
							)
						}
						Row(
							verticalAlignment = Alignment.CenterVertically
						) {
							Text(
								text = String.format("%.1f", filter.value.minScore),
								modifier = Modifier.widthIn(min = 32.dp),
								textAlign = TextAlign.Start
							)
							RangeSlider(
								values = filter.value.minScore..filter.value.maxScore,
								onValueChange = { selectedRange ->
									filter.value = filter.value.copy(minScore = (selectedRange.start * 10).roundToInt() / 10f, maxScore = (selectedRange.endInclusive * 10).roundToInt() / 10f)
								},
								valueRange = 0f..10f,
								modifier = Modifier.weight(1f)
							)
							Text(
								text = String.format("%.1f", filter.value.maxScore),
								modifier = Modifier.widthIn(min = 32.dp),
								textAlign = TextAlign.End
							)
						}
					}
				}
				
				HorizontalItemView(
					modifier = Modifier.clickable {
						filter.value = filter.value.copy(showEmptyMetrics = !filter.value.showEmptyMetrics)
					}
				) {
					Checkbox(
						checked = filter.value.showEmptyMetrics,
						onCheckedChange = null,
						colors = CheckboxDefaults.colors(checkedColor = MaterialTheme.colors.primary)
					)
					Spacer(modifier = Modifier.width(16.dp))
					Text(
						text = stringResource(id = R.string.view_listing_filters_show_empty_metrics)
					)
				}
				
				HorizontalItemView {
					Column {
						Row(
							modifier = Modifier.padding(bottom = 16.dp)
						) {
							Icon(imageVector = Icons.Filled.TextFields, contentDescription = stringResource(
								id = R.string.view_listing_filters_text_icon
							))
							Spacer(modifier = Modifier.width(16.dp))
							Text(
								text = stringResource(id = R.string.view_listing_filters_text)
							)
						}
						Row {
							TextField(value = filter.value.text, onValueChange = { str ->
								filter.value = filter.value.copy(text = str)
							}, modifier = Modifier.weight(1f))
						}
					}
				}
				
				Spacer(modifier = Modifier.height(bottomPadding))
			}
		}
	}
}