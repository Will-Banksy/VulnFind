package com.willbanksy.vulnfind.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.willbanksy.vulnfind.R
import java.time.LocalDateTime
import java.time.Month
import java.time.Year

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun DatePickerDialog(
	showing: MutableState<Boolean>,
	initialYear: Year?,
	initialMonth: Month?,
	yearRange: IntRange = 1970..LocalDateTime.now().year,
	onDateChange: (year: Year?, month: Month?) -> Unit = { _, _ -> }
) {
	if(showing.value) {
		Dialog(
			properties = DialogProperties(usePlatformDefaultWidth = false),
			onDismissRequest = {
				showing.value = false
			}
		) {
			BoxWithConstraints {
				val maxHeight = maxHeight
				Surface(
					elevation = 4.dp,
					shape = RoundedCornerShape(4.dp),
					modifier = Modifier
						.heightIn(max = maxHeight * 0.8f)
						.widthIn(max = maxWidth * 0.8f)
				) {
					val selectedYear = remember {
						mutableStateOf(initialYear)
					}
					val selectedMonth = remember {
						mutableStateOf(initialMonth)
					}
					Column {
						val headerContent: @Composable (Boolean, Boolean) -> Unit = { stretchSpacing, reverse ->
							val selectDateText: @Composable () -> Unit = {
								Text(
									text = stringResource(id = R.string.view_datepicker_select_date).uppercase(),
									color = MaterialTheme.colors.onPrimary,
									style = MaterialTheme.typography.caption,
									modifier = Modifier.padding(16.dp)
								)
							}
							if(!reverse) {
								selectDateText()
								if(stretchSpacing) {
									Spacer(modifier = Modifier.weight(1f))
								}
							}
							val monthYearTextFn: @Composable () -> String = {
								val sb = StringBuilder()
								var month = false
								if(selectedMonth.value != null) {
									sb.append(localisedMonth(month = selectedMonth.value!!).uppercase())
									month = true
								}
								if(selectedYear.value != null) {
									if(month) {
										sb.append(", ")
									}
									sb.append(selectedYear.value.toString())
								}
								sb.toString()
							}
							val monthYearText = monthYearTextFn()
							val yearMonthStr = monthYearText.ifEmpty {
								stringResource(
									id = R.string.view_datepicker_no_selection
								)
							}
							Text(
								text = yearMonthStr,
								color = MaterialTheme.colors.onPrimary,
								style = MaterialTheme.typography.h5,
								modifier = Modifier.padding(16.dp)
							)
							if(reverse) {
								if(stretchSpacing) {
									Spacer(modifier = Modifier.weight(1f))
								}
								selectDateText()
							}
						}
						if(maxHeight >= 400.dp) {
							Column(
								modifier = Modifier
									.background(MaterialTheme.colors.primary)
									.fillMaxWidth()
							) {
								headerContent(false, false)
							}
						} else {
							Row(
								modifier = Modifier
									.background(MaterialTheme.colors.primary)
									.fillMaxWidth(),
								verticalAlignment = Alignment.CenterVertically
							) {
								headerContent(true, true)
							}
						}
						
						Column(
							modifier = Modifier.weight(1f)
						) {
							val showingYearSelection = remember {
								mutableStateOf(true)
							}

							val arrowRotation = remember {
								mutableStateOf(180f)
							}
							val animatedArrowRotation = animateFloatAsState(targetValue = arrowRotation.value)
							
							Row {
								Row(
									modifier = Modifier.clickable {
										showingYearSelection.value = !showingYearSelection.value
										arrowRotation.value = 180f * if (showingYearSelection.value) {
											1
										} else {
											0
										}
									},
								) {
									Box(
										modifier = Modifier
											.padding(8.dp)
											.weight(1f),
										contentAlignment = Alignment.Center
									) {
										Row {
											Text(
												text = stringResource(id = R.string.view_datepicker_year)
											)
											
											Icon(
												imageVector = Icons.Filled.ExpandMore,
												contentDescription = "",
												modifier = Modifier
													.scale(0.75f)
													.rotate(animatedArrowRotation.value)
											)
										}
									}
								}
							}
							
							Box {
								DatePickerDialogMonthView(selectedMonth = selectedMonth)
								this@Column.AnimatedVisibility(visible = showingYearSelection.value) {
									DatePickerDialogYearView(yearRange, selectedYear)
								}
							}
						}
						Divider()
						Row(
							modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp)
						) {
							Spacer(modifier = Modifier.weight(1f))
							TextButton(onClick = {
								showing.value = false
							}) {
								Text(text = stringResource(R.string.view_cancel).uppercase())
							}
							Spacer(modifier = Modifier.width(16.dp))
							Button(onClick = {
								onDateChange(selectedYear.value, selectedMonth.value)
								showing.value = false
							}) {
								Text(text = stringResource(R.string.view_ok).uppercase())
							}
						}
					}
				}
			}
		}
	}
}

@Composable
fun DatePickerDialogYearView(yearRange: IntRange, selectedYear: MutableState<Year?>) {
	Surface {
		LazyVerticalGrid(
			columns = GridCells.Adaptive(80.dp),
			contentPadding = PaddingValues(16.dp),
			userScrollEnabled = true,
			content = {
				val years = yearRange.toList()
				items(items = years) { year ->
					MonthYearPillboxView({
						selectedYear.value = Year.of(year)
					}, year.toString())
				}
				
				item {
					MonthYearPillboxView(onClick = {
				   		selectedYear.value = null
					}, text = stringResource(id = R.string.view_datepicker_deselect))
				}
			}
		)
	}
}

@Composable
fun DatePickerDialogMonthView(selectedMonth: MutableState<Month?>) {
	LazyVerticalGrid(
		columns = GridCells.Adaptive(128.dp),
		contentPadding = PaddingValues(16.dp),
		userScrollEnabled = true,
		content = {
			val months = Month.values()
			items(items = months) { month ->
				MonthYearPillboxView({
					selectedMonth.value = month
				}, localisedMonth(month = month))
			}
			
			item {
				MonthYearPillboxView(onClick = {
					selectedMonth.value = null
				}, text = stringResource(id = R.string.view_datepicker_deselect))
			}
		}
	)
}

@Composable
fun MonthYearPillboxView(onClick: () -> Unit, text: String) { // TODO https://stackoverflow.com/questions/1038570/how-can-i-convert-an-integer-to-localized-month-name-in-java
	Surface(
		shape = RoundedCornerShape(50)
	) {
		Box(
			modifier = Modifier.clickable {
				onClick()
			},
		) {
			Box(
				modifier = Modifier
					.padding(8.dp)
					.fillMaxSize(),
				contentAlignment = Alignment.Center
			) {
				Text(
					text = text
				)
			}
		}
	}
}

@Composable
fun localisedMonth(month: Month): String {
	return when(month) {
		Month.JANUARY -> stringResource(id = R.string.view_datepicker_january)
		Month.FEBRUARY -> stringResource(id = R.string.view_datepicker_february)
		Month.MARCH -> stringResource(id = R.string.view_datepicker_march)
		Month.APRIL -> stringResource(id = R.string.view_datepicker_april)
		Month.MAY -> stringResource(id = R.string.view_datepicker_may)
		Month.JUNE -> stringResource(id = R.string.view_datepicker_june)
		Month.JULY -> stringResource(id = R.string.view_datepicker_july)
		Month.AUGUST -> stringResource(id = R.string.view_datepicker_august)
		Month.SEPTEMBER -> stringResource(id = R.string.view_datepicker_september)
		Month.OCTOBER -> stringResource(id = R.string.view_datepicker_october)
		Month.NOVEMBER -> stringResource(id = R.string.view_datepicker_november)
		Month.DECEMBER -> stringResource(id = R.string.view_datepicker_december)
	}
}