package com.willbanksy.vulnfind.ui

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

@Composable
fun VulnItem(title: String, description: String) {
    Surface(
        elevation = 2.dp,
        shape = RoundedCornerShape(CornerSize(2.dp)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.h6
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = description,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                fontStyle = FontStyle.Italic, // Doesn't seem to work on my phone with Samsung Sans unless I change my system font to Default
                color = MaterialTheme.colors.onBackground.copy(alpha = 0.5f)
            )
        }
    }
}