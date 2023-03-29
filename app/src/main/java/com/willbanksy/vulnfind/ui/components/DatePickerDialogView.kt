package com.willbanksy.vulnfind.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Expand
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import kotlinx.coroutines.launch
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
									text = "SELECT DATE",
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
							val sb = StringBuilder()
							if(selectedYear.value != null) {
								sb.append(selectedYear.value.toString())
								if(selectedMonth.value != null) {
									sb.append(", ")
								}
							}
							if(selectedMonth.value != null) {
								sb.append(selectedMonth.value)
							}
							Text(
								text = sb.toString(),
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
						Column {
							val showingYearSelection = remember {
								mutableStateOf(false)
							}
							
							Row {
								Row(
									modifier = Modifier.clickable {
										showingYearSelection.value = !showingYearSelection.value
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
												text = selectedYear.value.toString()
											)
											Icon(
												imageVector = Icons.Filled.ExpandMore,
												contentDescription = "",
												modifier = Modifier
													.scale(0.75f)
													.rotate(
														180f * if (showingYearSelection.value) {
															1
														} else {
															0
														}
													)
											)
										}
									}
								}
							}
							
							Box(/*modifier = Modifier.fillMaxSize()*/) {
								DatePickerDialogMonthView(selectedMonth = selectedMonth)
								this@Column.AnimatedVisibility(visible = showingYearSelection.value) {
									DatePickerDialogYearView(yearRange, selectedYear)
								}
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
			content = {
				val years = yearRange.toList()
				items(items = years) { year ->
					Surface(
						shape = RoundedCornerShape(50)
					) {
						Box(
							modifier = Modifier.clickable {
								selectedYear.value = Year.of(year)
							},
						) {
							Box(
								modifier = Modifier
									.padding(8.dp)
									.fillMaxSize(),
								contentAlignment = Alignment.Center
							) {
								Text(
									text = year.toString()
								)
							}
						}
					}
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
		content = {
			val months = Month.values()
			items(items = months) { month ->
				Surface(
					shape = RoundedCornerShape(50)
				) {
					Box(
						modifier = Modifier.clickable {
							selectedMonth.value = month
						},
					) {
						Box(
							modifier = Modifier
								.padding(8.dp)
								.fillMaxSize(),
							contentAlignment = Alignment.Center
						) {
							Text(
								text = month.toString()
							)
						}
					}
				}
			}
		}
	)
}