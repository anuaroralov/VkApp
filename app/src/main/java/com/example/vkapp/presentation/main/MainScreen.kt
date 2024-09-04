package com.example.vkapp.presentation.main

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.vkapp.navigation.AppNavGraph
import com.example.vkapp.navigation.NavigationItem
import com.example.vkapp.navigation.Screen
import com.example.vkapp.navigation.rememberNavigationState
import com.example.vkapp.presentation.ViewModelFactory
import com.example.vkapp.presentation.home.HomeScreen
import com.example.vkapp.presentation.home.comments.CommentsScreen
import com.vk.id.VKIDUser

@Composable
fun MainScreen(user: VKIDUser?, viewModelFactory: ViewModelFactory) {
    val navigationState = rememberNavigationState()
    val navBackStackEntry by navigationState.navHostController.currentBackStackEntryAsState()

    val showBottomBar = navBackStackEntry?.destination?.route != Screen.Comments.route

    val context = LocalContext.current

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(
                        0.000000000000000000000000000000000000000000001.dp
                    ),
                    contentColor = MaterialTheme.colorScheme.onSurface
                ) {
                    val items = listOf(
                        NavigationItem.Home,
                        NavigationItem.Messages,
                        NavigationItem.Profile
                    )
                    items.forEach { item ->
                        val selected = navBackStackEntry?.destination?.hierarchy?.any {
                            it.route == item.screen.route
                        } ?: false

                        NavigationBarItem(
                            selected = selected,
                            onClick = {
                                navigationState.navigateTo(item.screen.route)
                            },
                            icon = {
                                if (item.icon is ImageVector) {
                                    Icon(
                                        imageVector = item.icon,
                                        contentDescription = stringResource(item.title)
                                    )
                                } else if (item.icon is Int) {
                                    Icon(
                                        painter = painterResource(id = item.icon),
                                        contentDescription = stringResource(item.title)
                                    )
                                }
                            },
                            label = { Text(text = stringResource(item.title)) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = MaterialTheme.colorScheme.primary,
                                unselectedIconColor = MaterialTheme.colorScheme.onSurface,
                                selectedTextColor = MaterialTheme.colorScheme.primary,
                                unselectedTextColor = MaterialTheme.colorScheme.onSurface,
                                indicatorColor = Color.Transparent
                            ),
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        AppNavGraph(
            navHostController = navigationState.navHostController,
            newsFeedScreenContent = {
                HomeScreen(
                    paddingValues = paddingValues,
                    onCommentClickListener = {
                        navigationState.navigateToComments(it)
                    },
                    onLinkClickListener = { url ->
                        openUrl(url, context = context)
                    },
                    user = user, viewModelFactory = viewModelFactory
                )
            },
            commentsScreenContent = { feedPost ->
                CommentsScreen(
                    onBackPressed = {
                        navigationState.navHostController.popBackStack()
                    },
                    feedPost = feedPost
                )
            },
            favouriteScreenContent = { Text("Favourite") },
            profileScreenContent = { Text("Profile") }
        )
    }
}


fun openUrl(url: String, context: Context) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    context.startActivity(intent)
}

