package com.willbanksy.vulnfind.ui.state

import java.time.Month
import java.time.Year

data class ListingFilter(
	val month: Month? = null,
	val year: Year? = null
) {
	fun numFilters(): Int {
		var ret = 0
		if(month != null) ret += 1
		if(year != null) ret += 1
		return ret
	}
}