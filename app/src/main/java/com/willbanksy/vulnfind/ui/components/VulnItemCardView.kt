package com.willbanksy.vulnfind.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.willbanksy.vulnfind.data.VulnDataItem
import com.willbanksy.vulnfind.utils.pickPrimaryMetric

@Composable
fun VulnItemCardView(modifier: Modifier, vuln: VulnDataItem) {
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
			Row(
				verticalAlignment = Alignment.CenterVertically
			) {
				Text(
					text = vuln.cveId,
					style = MaterialTheme.typography.h5
				)
				Spacer(modifier = Modifier.width(8.dp))
				Spacer(modifier = Modifier.weight(1f))
				val metric = pickPrimaryMetric(vuln.metrics)
				val baseScore: Float? = metric?.baseScore
				if(baseScore != null) {
					CvssRatingChipView(metric.version, baseScore)
				}
			}
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = vuln.description,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                fontStyle = FontStyle.Italic,
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.5f)
            )
        }
    }
}