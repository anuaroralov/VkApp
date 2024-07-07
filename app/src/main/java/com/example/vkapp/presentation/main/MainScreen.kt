package com.example.vkapp.presentation.main

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.vkapp.navigation.AppNavGraph
import com.example.vkapp.navigation.NavigationItem
import com.example.vkapp.navigation.rememberNavigationState
import com.example.vkapp.presentation.comments.CommentsScreen
import com.example.vkapp.presentation.home.HomeScreen

@Preview(showBackground = true)
@Composable
fun MainScreen() {
    val navigationState = rememberNavigationState()

    Scaffold(bottomBar = {
        NavigationBar(
            containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(
                0.000000000000000000000000000000000000000000001.dp
            ),
            contentColor = MaterialTheme.colorScheme.onSurface
        ) {

            val navBackStackEntry by navigationState.navHostController.currentBackStackEntryAsState()

            val items =
                listOf(NavigationItem.Home, NavigationItem.Favourite, NavigationItem.Profile)
            items.forEach { item ->
                val selected = navBackStackEntry?.destination?.hierarchy?.any {
                    it.route == item.screen.route
                } ?: false

                NavigationBarItem(
                    selected = selected,
                    onClick = {
                        navigationState.navigateTo(item.screen.route)
                    },
                    icon = { Icon(item.icon, contentDescription = stringResource(item.title)) },
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
    }) { paddingValues ->
        AppNavGraph(
            navHostController = navigationState.navHostController,
            newsFeedScreenContent = {
                HomeScreen(
                    paddingValues = paddingValues,
                    onCommentClickListener = {
                        navigationState.navigateToComments(it)
                    }
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