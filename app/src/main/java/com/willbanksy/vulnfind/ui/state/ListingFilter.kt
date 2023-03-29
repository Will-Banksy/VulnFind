package com.willbanksy.vulnfind.ui.state

import java.time.Month
import java.time.Year

data class ListingFilter(
	val month: Month? = null,
	val year: Year? = null
)