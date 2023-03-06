package com.willbanksy.vulnfind.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorPalette = darkColors( // TODO: Tweak dark theme colours
	primary = Purple200,
	primaryVariant = Purple700,
	secondary = Teal200,
	background = Color.Black,
	surface = Color.Black
)

private val LightColorPalette = lightColors(
	primary = Purple500,
	primaryVariant = Purple700,
	secondary = Teal200

	/* Other default colors to override
	background = Color.White,
	surface = Color.White,
	onPrimary = Color.White,
	onSecondary = Color.Black,
	onBackground = Color.Black,
	onSurface = Color.Black,
	*/
)

@Composable
fun VulnFindTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) { // TODO: Disable light mode?
	val colors = if (darkTheme) {
		DarkColorPalette
	} else {
		LightColorPalette
	}
	
	val view = LocalView.current
	if(!view.isInEditMode) {
		SideEffect {
			val window = (view.context as Activity).window
			val insets = WindowCompat.getInsetsController(window, view)
			window.statusBarColor = Color.Transparent.toArgb() // colors.background.toArgb() // choose a status bar color
			window.navigationBarColor = Color.Transparent.toArgb() // colors.background.toArgb() // choose a navigation bar color
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
				window.setDecorFitsSystemWindows(false)
			}
			insets.isAppearanceLightStatusBars = !darkTheme
			insets.isAppearanceLightNavigationBars = !darkTheme
		}
	}

	MaterialTheme(
		colors = colors,
		typography = Typography,
		shapes = Shapes,
		content = content
	)
}