package com.willbanksy.vulnfind.ui.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.willbanksy.vulnfind.models.MainViewModel
import com.willbanksy.vulnfind.R
import com.willbanksy.vulnfind.data.VulnDataItemMetric
import com.willbanksy.vulnfind.data.VulnDataItemReference
import com.willbanksy.vulnfind.utils.CvssSeverity
import com.willbanksy.vulnfind.utils.cvssColourForSeverity
import com.willbanksy.vulnfind.utils.pickPrimaryMetricId

@Composable
fun VulnDetailView(model: MainViewModel, cveId: String) {
	val vulnStream = remember {
		model.getById(cveId)
	}
	val vuln = vulnStream.collectAsState(initial = null).value
	val vulnTitle = vuln?.cveId ?: stringResource(id = R.string.view_loading_text)
	
	Box(
		modifier = Modifier
			.fillMaxSize()
	) {
		val scrollState = rememberScrollState()
		Column(
			modifier = Modifier
				.verticalScroll(scrollState)
				.padding(16.dp)
		) {
			Text(
				text = vulnTitle,
				style = MaterialTheme.typography.h4
			)
			if(vuln != null) {
				val primaryMetricId = pickPrimaryMetricId(vuln.metrics)
				if(primaryMetricId != -1) {
					Spacer(modifier = Modifier.height(8.dp))
					CvssRatingChipView(metricVersionString = vuln.metrics[primaryMetricId].version, baseScore = vuln.metrics[primaryMetricId].baseScore)
				}
				Spacer(modifier = Modifier.height(16.dp))
				Text(
					text = vuln.description
				)
				Spacer(modifier = Modifier.height(16.dp))
				Text(
					text = "${stringResource(id = R.string.view_details_published)}: ${vuln.publishedDate.replace('T', ' ')}"
				)
				Text(
					text = "${stringResource(id = R.string.view_details_modified)}: ${vuln.lastModifiedDate.replace('T', ' ')}"
				)
				Spacer(modifier = Modifier.height(16.dp))
				Text(
					text = "${stringResource(id = R.string.view_details_source_id)}: ${vuln.sourceId}"
				)
				
				if(primaryMetricId != -1) {
					Text(
						text = stringResource(id = R.string.view_details_heading_metrics),
						style = MaterialTheme.typography.h6,
						modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
					)
					MetricDetailView(m = vuln.metrics[primaryMetricId], primary = true,
						modifier = Modifier.fillMaxWidth()
					)
					for(i in vuln.metrics.indices) {
						if(i != primaryMetricId) {
							Spacer(modifier = Modifier.height(8.dp))
							MetricDetailView(m = vuln.metrics[i], modifier = Modifier.fillMaxWidth())
						}
					}
				}
				
				if(vuln.references.isNotEmpty()) {
					Text(
						text = stringResource(id = R.string.view_details_heading_references),
						style = MaterialTheme.typography.h6,
						modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
					)
					for(i in vuln.references.indices) {
						if(i != 0) {
							Spacer(modifier = Modifier.height(8.dp))
						}
						ReferenceDetailView(ref = vuln.references[i], modifier = Modifier.fillMaxWidth())
					}
				}
			}
			val bottomPadding = WindowInsets.systemBars.asPaddingValues().calculateBottomPadding()
			Spacer(modifier = Modifier.padding(bottom = bottomPadding))
		}
	}
}

@Composable
fun MetricDetailView(m: VulnDataItemMetric, modifier: Modifier = Modifier, primary: Boolean = false) {
	Surface(
		elevation = 2.dp,
		shape = RoundedCornerShape(CornerSize(2.dp)),
		modifier = modifier
	) {
		Column(
			modifier = Modifier.padding(8.dp)
		) {
			Row {
				val baseSeverity = if(m.baseSeverity.isEmpty() || m.baseSeverity.isBlank()) CvssSeverity.mapFrom(m.baseScore).toString() else m.baseSeverity
				Text(text = "${m.version}: ${m.baseScore} (${baseSeverity})")
				if(primary) {
					Spacer(modifier = Modifier.weight(1f))
					Icon(
						imageVector = Icons.Filled.Star,
						contentDescription = stringResource(id = R.string.view_details_primary_metric_icon),
						tint = cvssColourForSeverity(CvssSeverity.mapFrom(m.baseScore))
					)
				}
			}
			Spacer(modifier = Modifier.height(8.dp))
			Text(
				text = m.vectorString,
				style = MaterialTheme.typography.caption,
				color = MaterialTheme.colors.onSurface.copy(alpha = 0.5f)
			)
		}
	}
}

@Composable
fun ReferenceDetailView(ref: VulnDataItemReference, modifier: Modifier = Modifier) {
	Surface(
		elevation = 2.dp,
		shape = RoundedCornerShape(CornerSize(2.dp)),
		modifier = modifier
	) {
		Column(
			modifier.padding(8.dp)
		) {
			val annotatedStringLink: AnnotatedString = buildAnnotatedString {
				val str = ref.url
				append(str)
				addStyle(
					style = SpanStyle(
						color = MaterialTheme.colors.primary,
						textDecoration = TextDecoration.Underline
					), start = 0, end = str.length
				)
				addStringAnnotation(
					tag = "URL",
					annotation = str,
					start =  0,
					end = str.length
				)
			}
			Text(text = ref.source)

			val uriHandler = LocalUriHandler.current
			
			ClickableText(
				text = annotatedStringLink,
				style = MaterialTheme.typography.caption,
				onClick = {
					annotatedStringLink.getStringAnnotations("URL", it, it)
						.firstOrNull()?.let { stringAnnotation ->
							uriHandler.openUri(stringAnnotation.item)
						}
				}
			)

			if(ref.tags.isNotEmpty()) {
				Spacer(modifier = Modifier.height(8.dp))
				
				val scrollState = rememberScrollState()
				Row(
					modifier = Modifier
						.horizontalScroll(scrollState)
						.fillMaxWidth()
				) {
					var wasPrev = false
					for(tag in ref.tags) {
						if(tag.isBlank() || tag.isEmpty()) {
							continue
						}
						if(wasPrev) {
							Spacer(modifier = Modifier.width(8.dp))
						}
						Surface(
							elevation = 0.dp,
							color = MaterialTheme.colors.onSurface.copy(alpha = 0.05f),
							shape = RoundedCornerShape(50)
						) {
							Text(
								text = tag,
								modifier.padding(horizontal = 10.dp, vertical = 2.dp)
							)
						}
						wasPrev = true
					}
				}
			}
		}
	}
}