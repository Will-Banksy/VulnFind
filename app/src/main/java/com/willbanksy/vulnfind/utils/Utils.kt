package com.willbanksy.vulnfind.utils

import android.content.Context
import android.net.ConnectivityManager

fun isConnectionMetered(context: Context): Boolean {
	val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
	return connectivityManager.isActiveNetworkMetered
}