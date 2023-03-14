package com.willbanksy.vulnfind.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.willbanksy.vulnfind.R
import com.willbanksy.vulnfind.ui.components.TopBarView

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun AboutActivityView() {
	Surface(
		modifier = Modifier.fillMaxSize(),
		color = MaterialTheme.colors.background
	) {
		Scaffold(
			topBar = {
				TopBarView(label = stringResource(R.string.activity_about_title), true)
			}
		) {
			BoxWithConstraints {
				if(this.maxWidth > this.maxHeight) {
					Row(
						verticalAlignment = Alignment.CenterVertically
					) {
						AboutIconView(false, Modifier.weight(1f, false))
						AboutTextView(Modifier.weight(1f, true))
					}
				} else {
					Column(
						horizontalAlignment = Alignment.CenterHorizontally
					) {
						AboutIconView(true, Modifier.weight(1f, false))
						AboutTextView(Modifier.weight(1f, true))
					}
				}
			}
		}
	}
}

@Composable
fun AboutIconView(vertical: Boolean, modifier: Modifier) {
	Column(
		modifier = Modifier.padding(16.dp).then(modifier)
	) {
		Icon(
			painter = painterResource(R.drawable.vulnfind_launcher_icon_foreground),
			contentDescription = stringResource(R.string.app_name),
			modifier = Modifier
				.aspectRatio(1f, !vertical),
			tint = Color.Unspecified
		)
	}
}

@Composable
fun AboutTextView(modifier: Modifier) {
	Column(
		horizontalAlignment = Alignment.CenterHorizontally,
		modifier = Modifier
			.fillMaxWidth()
			.padding(16.dp)
			.then(modifier)
	) {
		Text(
			text = stringResource(R.string.app_name),
			style = MaterialTheme.typography.h4
		)
		Spacer(modifier = Modifier.height(4.dp))
		Text(
			text = "${stringResource(R.string.view_about_version_prefix)} ${stringResource(R.string.app_version_string)}"
		)
		Spacer(modifier = Modifier.height(16.dp))
		Text(
			text = stringResource(R.string.view_about_disclaimer),
			color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f),
			textAlign = TextAlign.Center
		)
	}
}