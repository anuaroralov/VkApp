package com.example.vkapp.ui.theme

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.vkapp.navigation.AppNavGraph
import com.example.vkapp.navigation.NavigationItem
import com.example.vkapp.navigation.rememberNavigationState

@Preview(showBackground = true)
@Composable
fun MainScreen() {
    val navigationState = rememberNavigationState()

    Scaffold(bottomBar = {
        NavigationBar(containerColor = MaterialTheme.colorScheme.primary) {

            val navBackStackEntry by navigationState.navHostController.currentBackStackEntryAsState()

            val items =
                listOf(NavigationItem.Home, NavigationItem.Favourite, NavigationItem.Profile)
            items.forEach { item ->
                val selected = navBackStackEntry?.destination?.hierarchy?.any{
                    it.route == item.screen.route
                }?: false

                NavigationBarItem(
                    selected = selected,
                    onClick = {
                        navigationState.navigateTo(item.screen.route)
                    },
                    icon = { Icon(item.icon, contentDescription = item.title) },
                    label = { Text(text = item.title) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.onPrimary,
                        unselectedIconColor = MaterialTheme.colorScheme.onSecondary
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
            commentsScreenContent ={feedPost->
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