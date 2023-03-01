package com.willbanksy.vulnfind.ui

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.willbanksy.vulnfind.data.VulnItemWithMetrics

@Composable
fun VulnItemCardView(modifier: Modifier, vuln: VulnItemWithMetrics) {
    Surface(
        elevation = 2.dp,
        shape = RoundedCornerShape(CornerSize(2.dp)),
        modifier = Modifier
			.fillMaxWidth()
			.then(modifier)
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
			Row {
				Text(
					text = vuln.item.cveId,
					style = MaterialTheme.typography.h5
				)
				val baseScore: Float? = vuln.metrics.getOrNull(0)?.baseScore
				Log.d("METRICS", vuln.metrics.toString())
//				if(baseScore != null) {
					val background = if((baseScore ?: 0f) >= 7.5f) {
						MaterialTheme.colors.error
					} else {
						MaterialTheme.colors.surface
					}
					val foreground = if((baseScore ?: 0f) >= 7.5f) {
						MaterialTheme.colors.onError
					} else {
						MaterialTheme.colors.onSurface
					}
					Box {
						Text(
							text = baseScore.toString(),
							modifier = Modifier.background(background),
							color = foreground
						)
					}
//				}
			}
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = vuln.item.description,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                fontStyle = FontStyle.Italic, // Doesn't seem to work on my phone with Samsung Sans unless I change my system font to Default
                color = MaterialTheme.colors.onBackground.copy(alpha = 0.5f)
            )
        }
    }
}