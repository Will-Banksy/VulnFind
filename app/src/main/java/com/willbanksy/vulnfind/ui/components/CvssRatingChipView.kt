package com.willbanksy.vulnfind.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.willbanksy.vulnfind.utils.CvssSeverity
import com.willbanksy.vulnfind.utils.cvssColourForSeverity

@Composable
fun CvssRatingChipView(metricVersionString: String, baseScore: Float) {
	val severity = CvssSeverity.mapFrom(baseScore)
	val foreground = cvssColourForSeverity(severity)
	val background = foreground.copy(alpha = 0x2f.toFloat() / 255)
	Surface(
		shape = RoundedCornerShape(CornerSize(50))
	) {
		Box(modifier = Modifier.background(background)) {
			Text(
				text = "${metricVersionString}: $baseScore ($severity)",
				modifier = Modifier.padding(horizontal = 10.dp, vertical = 2.dp),
				color = foreground,
				maxLines = 1,
				overflow = TextOverflow.Ellipsis
			)
		}
	}
}