package com.willbanksy.vulnfind.ui.components

import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import com.willbanksy.vulnfind.ListingActivity
import com.willbanksy.vulnfind.R
import com.willbanksy.vulnfind.models.MainViewModel

@Composable
fun HomeScreenView(model: MainViewModel) {
	Column {
		val context = LocalContext.current
		val intent = Intent(context, ListingActivity::class.java)
		Row(
			modifier = Modifier
				.fillMaxWidth()
				.clickable {
					startActivity(context, intent, null)
				}
		) {
			Row(
				modifier =  Modifier.padding(16.dp)
			) {
				Icon(
					imageVector = Icons.Filled.List,
					contentDescription = stringResource(R.string.view_home_listing_icon_name),
					tint = MaterialTheme.colors.onSurface
				)
				Spacer(modifier = Modifier.width(16.dp))
				Text(text = stringResource(R.string.view_home_listing_title))
			}
		}
	}
}