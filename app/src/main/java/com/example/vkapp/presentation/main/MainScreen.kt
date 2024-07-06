package com.example.vkapp.presentation.main

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
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
        NavigationBar(containerColor = MaterialTheme.colorScheme.surface) {

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
                        unselectedIconColor = MaterialTheme.colorScheme.onSurface
                    )
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