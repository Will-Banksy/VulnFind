package com.willbanksy.vulnfind.ui

import android.annotation.SuppressLint
import androidx.annotation.StringRes
import androidx.compose.animation.*
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.willbanksy.vulnfind.R
import com.willbanksy.vulnfind.model.VulnListModel
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import soup.compose.material.motion.animation.materialFadeThroughIn
import soup.compose.material.motion.animation.materialFadeThroughOut

sealed class NavigationDestination(val route: String, @StringRes val labelTextRes: Int, @StringRes val titleRes: Int, val icon: ImageVector) {
	object VulnList : NavigationDestination("vulnlist", R.string.label_vulnlist, R.string.title_vulnlist, Icons.Filled.List)
	object VulnSearch : NavigationDestination("vulnsearch", R.string.label_vulnsearch, R.string.title_vulnsearch, Icons.Filled.Search)
}

val navItems = listOf(
	NavigationDestination.VulnList,
	NavigationDestination.VulnSearch
)

@OptIn(ExperimentalAnimationApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MainActivityView(model: VulnListModel) {
	Surface(
		modifier = Modifier.fillMaxSize(),
		color = MaterialTheme.colors.background
	) {
		val navController = rememberAnimatedNavController()
		Scaffold(
			topBar = {
				val navBackStackEntry by navController.currentBackStackEntryAsState()
				val currentDestination = navBackStackEntry?.destination
				
				val label = navItems.find { dest ->
					currentDestination?.hierarchy?.any { it.route == dest.route } == true
				}.let { 
					if(it == null) {
						return@let "ERROR: No Valid Route"
					} else {
						return@let stringResource(id = it.titleRes)
					}
				}
				
				val topPadding = WindowInsets.systemBars.asPaddingValues().calculateTopPadding()
				TopAppBar(
					contentPadding = PaddingValues(top = topPadding)
				) {
					Text(
						text = label,
						style = MaterialTheme.typography.h6,
						color = MaterialTheme.colors.onSurface,
						modifier = Modifier.padding(16.dp, 0.dp)
					)
				}
			},
			bottomBar = {
				val bottomPadding = WindowInsets.systemBars.asPaddingValues().calculateBottomPadding()
				BottomAppBar(
					contentPadding = PaddingValues(bottom = bottomPadding)
				) {
					BottomNavigation {
						val navBackStackEntry by navController.currentBackStackEntryAsState()
						val currentDestination = navBackStackEntry?.destination
						navItems.forEach { navDest ->
							BottomNavigationItem(
								icon = { Icon(imageVector = navDest.icon, contentDescription = null)},
								label = { Text(text = stringResource(id = navDest.labelTextRes)) },
								selected = currentDestination?.hierarchy?.any { it.route == navDest.route } == true,
								onClick = {
									navController.navigate(navDest.route) {
										// Pop up to the start destination of the graph to avoid building up a large stack of destinations
										// TODO: Evaluate whether to use this. This means that you cannot go "back" from the start to not the start. Maybe modify it to be smarter e.g. remove all duplicate (source -> destination)s
										popUpTo(navController.graph.findStartDestination().id) {
											saveState = true
										}
										// Avoid multiple copies of the same destination
										launchSingleTop = true
										// Restore state when selecting a previously selected item
										restoreState = true
									}
								}
							)
						}
					}
				}
			}
		) {
			AnimatedNavHost(
				modifier = Modifier.padding(it),
				navController = navController,
				startDestination = NavigationDestination.VulnList.route,
				enterTransition = { materialFadeThroughIn() },
				exitTransition = { materialFadeThroughOut() }
			) {
				composable(route = NavigationDestination.VulnList.route) { VulnListView(model) }
				composable(route = NavigationDestination.VulnSearch.route) { VulnSearchView(model) }
			}
		}
	}
}