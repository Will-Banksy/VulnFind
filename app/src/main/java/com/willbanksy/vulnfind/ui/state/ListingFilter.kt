package com.willbanksy.vulnfind.ui.state

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.Month
import java.time.Year

@Parcelize
data class ListingFilter(
	val month: Month? = null,
	val year: Year? = null,
	val minScore: Float = 0f,
	val maxScore: Float = 10f,
	val text: String = "",
	val showEmptyMetrics: Boolean = true
): Parcelable {
	fun numFilters(): Int {
		var ret = 0
		if(month != null) ret += 1
		if(year != null) ret += 1
		if(minScore != 0f || maxScore != 10f) ret += 1
		if(text != "") ret += 1
		if(!showEmptyMetrics) ret += 1
		return ret
	}
}