package com.willbanksy.vulnfind.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.willbanksy.vulnfind.models.MainViewModel
import com.willbanksy.vulnfind.ui.state.ListingFilter
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.Year

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ListFilterSheetView(model: MainViewModel, bottomSheetState: BottomSheetState, filter: MutableState<ListingFilter>) {
//	val firstYearStart = ZonedDateTime.of(1988, 1, 1, 0, 0, 0, 0, ZoneId.ofOffset("UTC", ZoneOffset.UTC))
//	val firstYearEnd = ZonedDateTime.of(1988, 12, 31, 23, 59, 59, 0, ZoneId.ofOffset("UTC", ZoneOffset.UTC))
	
	val bottomPadding = WindowInsets.systemBars.asPaddingValues().calculateBottomPadding()
	val topPadding = WindowInsets.systemBars.asPaddingValues().calculateTopPadding()
	Column {
		Column(
			modifier = Modifier.background(MaterialTheme.colors.surface)//Color.Red.copy(alpha = 0.2f))
		) {
			val fractionToExpanded = if(bottomSheetState.progress.fraction == 0f) {
				if(bottomSheetState.isExpanded) { 1f } else { 0f }
			} else {
				if(bottomSheetState.progress.to == BottomSheetValue.Expanded) {
					bottomSheetState.progress.fraction
				} else {
					1 - bottomSheetState.progress.fraction
				}
			}
			Row(
				modifier = Modifier
					.fillMaxWidth()
					.height(64.dp)
					.padding(horizontal = 16.dp)
					.offset(y = topPadding * fractionToExpanded),
				verticalAlignment = Alignment.CenterVertically
			) {
				val sb = StringBuilder()
				sb.append("Filters ") // TODO: strings.xml key
				var filters = false
				if(filter.value.month != null) {
					sb.append('(')
					sb.append(filter.value.month.toString())
					filters = true
				}
				if(filter.value.year != null) {
					if(!filters) {
						sb.append('(')
					}
					sb.append(filter.value.year.toString())
					filters = true
				}
				if(filters) {
					sb.append(')')
				}
				Icon(imageVector = Icons.Filled.FilterAlt, contentDescription = "")
				Spacer(modifier = Modifier.width(16.dp))
				Text(
					text = sb.toString(),
					style = MaterialTheme.typography.h6,
					maxLines = 1,
					overflow = TextOverflow.Ellipsis
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
				}) {
					Icon(
						imageVector = Icons.Filled.ExpandLess,
						contentDescription = "",
						modifier = Modifier.rotate(180f * fractionToExpanded)
					)
				}
			}
			Spacer(modifier = Modifier.height(topPadding))
		}
		
		val dialogVisibility = remember {
			mutableStateOf(false)
		}
		DatePickerDialog(
			showing = dialogVisibility,
			initialYear = Year.of(LocalDateTime.now().year),
			initialMonth = LocalDateTime.now().month,
			yearRange = 1988..LocalDateTime.now().year,
			onDateChange = { year, month ->
				filter.value = filter.value.copy(year = year, month = month)
			}
		)
		
		Button(onClick = {
			dialogVisibility.value = true
		}) {
			Text(text = "PICK DATE")
		}
		
//		val startYear = 1988
//		val currYear = LocalDateTime.now().year
//		val years = (startYear..currYear).toList()
//		
//		val currMonth = LocalDateTime.now().month
//		
//		LazyColumn(
//			contentPadding = PaddingValues(top = topPadding, bottom = bottomPadding),
//			userScrollEnabled = bottomSheetState.isExpanded
//		) {
//			items(years) { year ->
//				Box(modifier = Modifier.clickable {
//					filter.value = filter.value.copy(year = Year.of(year))
//				}) {
//					Box(
//						modifier = Modifier
//							.fillMaxWidth()
//							.padding(16.dp)
//					) {
//						Text(
//							text = year.toString()
//						)
//					}
//				}
//			}
//		}
	}
}