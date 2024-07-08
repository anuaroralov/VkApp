package com.example.vkapp.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import com.example.vkapp.domain.FeedPost
import com.example.vkapp.navigation.Screen.Companion.KEY_FEED_POST

@Composable
fun AppNavGraph(
    navHostController: NavHostController,
    newsFeedScreenContent: @Composable () -> Unit,
    commentsScreenContent: @Composable (FeedPost) -> Unit,
    favouriteScreenContent: @Composable () -> Unit,
    profileScreenContent: @Composable () -> Unit,
) {
    NavHost(
        navController = navHostController,
        startDestination = Screen.Home.route
    ) {
        navigation(
            startDestination = Screen.NewsFeed.route,
            route = Screen.Home.route
        ) {
            composable(
                Screen.NewsFeed.route,
                enterTransition = { EnterTransition.None },
                exitTransition = { ExitTransition.None }
            ) {
                newsFeedScreenContent()
            }
            composable(
                route = Screen.Comments.route,
                arguments = listOf(
                    navArgument(KEY_FEED_POST) {
                        type = FeedPost.NavigationType
                    }
                ),
                enterTransition = {
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Up,
                        animationSpec = tween(700)
                    )
                },
                exitTransition = {
                    slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Down,
                        animationSpec = tween(700)
                    )
                },
                popEnterTransition = {
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Up,
                        animationSpec = tween(700)
                    )
                },
                popExitTransition = {
                    slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Down,
                        animationSpec = tween(700)
                    )
                }
            ) {
                val feedPost = it.arguments?.getParcelable<FeedPost>(KEY_FEED_POST)
                    ?: throw RuntimeException("Args is null")
                commentsScreenContent(feedPost)
            }
        }
        composable(
            Screen.Favourite.route,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None }
        ) {
            favouriteScreenContent()
        }
        composable(
            Screen.Profile.route,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None }
        ) {
            profileScreenContent()
        }
    }
}

