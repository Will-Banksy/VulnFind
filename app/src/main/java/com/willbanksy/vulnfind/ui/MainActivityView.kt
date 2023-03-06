package com.willbanksy.vulnfind.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.result.ActivityResultLauncher
import androidx.annotation.StringRes
import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.willbanksy.vulnfind.R
import com.willbanksy.vulnfind.model.VulnListModel
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.willbanksy.vulnfind.workers.DownloadWorker
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
fun MainActivityView(model: VulnListModel, notifPermissionRequest: ActivityResultLauncher<String>) {
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
					contentPadding = PaddingValues(top = topPadding, start = 8.dp, end = 8.dp),
					elevation = 0.dp
				) {
					Text(
						text = label,
						style = MaterialTheme.typography.h6,
						color = MaterialTheme.colors.onSurface,
						modifier = Modifier.padding(start = 8.dp)
					)
					Spacer(modifier = Modifier.weight(1f))
					
					val context = LocalContext.current
					IconButton(onClick = {
						if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
							if(context.checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED){
								val work: WorkRequest = OneTimeWorkRequestBuilder<DownloadWorker>().build()
								WorkManager.getInstance(context).enqueue(work)
							} else {
								notifPermissionRequest.launch(Manifest.permission.POST_NOTIFICATIONS)
							}
						}
					}) {
						Icon(
							imageVector = Icons.Filled.Refresh,
							contentDescription = "Refresh database",
							tint = Color.White
						)
					}
				}
			},
			bottomBar = {
				val bottomPadding = WindowInsets.systemBars.asPaddingValues().calculateBottomPadding()
				BottomAppBar(
					contentPadding = PaddingValues(bottom = bottomPadding, start = 8.dp, end = 8.dp),
					elevation = 0.dp
				) {
					IconButton(onClick = { /*TODO*/ }) {
						Icon(
							imageVector = Icons.Filled.Menu,
							contentDescription = stringResource(R.string.view_menu_button),
							tint = Color.White
						)
					}
					Spacer(modifier = Modifier.weight(1f))
					IconButton(onClick = { /*TODO*/ }) {
						Icon(
							imageVector = Icons.Filled.Search,
							contentDescription = stringResource(id = R.string.view_search_button),
							tint = Color.White
						)
					}
//					BottomNavigation(
//						elevation = 0.dp
//					) {
//						val navBackStackEntry by navController.currentBackStackEntryAsState()
//						val currentDestination = navBackStackEntry?.destination
//						navItems.forEach { navDest ->
//							BottomNavigationItem(
//								icon = { Icon(imageVector = navDest.icon, contentDescription = null) },
//								label = { Text(text = stringResource(id = navDest.labelTextRes)) },
//								selected = currentDestination?.hierarchy?.any { it.route == navDest.route } == true,
//								onClick = {
//									navController.navigate(navDest.route) {
//										// Pop up to the start destination of the graph to avoid building up a large stack of destinations
//										popUpTo(navController.graph.findStartDestination().id) {
//											saveState = true
//										}
//										// Avoid multiple copies of the same destination
//										launchSingleTop = true
//										// Restore state when selecting a previously selected item
//										restoreState = true
//									}
//								}
//							)
//						}
//					}
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