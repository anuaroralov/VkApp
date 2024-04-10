package com.example.vkapp

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Preview(showBackground = true)
@Composable
fun MainScreen(viewModel: MyViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {
    Scaffold(bottomBar = {
        NavigationBar(containerColor = MaterialTheme.colorScheme.primary) {
            val selectedItem = remember { mutableStateOf(0) }
            val items =
                listOf(NavigationItem.Home, NavigationItem.Favourite, NavigationItem.Profile)
            items.forEachIndexed { index, navigationItem ->
                NavigationBarItem(
                    selected = selectedItem.value == index,
                    onClick = { selectedItem.value = index },
                    icon = { Icon(navigationItem.icon, contentDescription = navigationItem.title) },
                    label = { Text(text = navigationItem.title) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.onPrimary,
                        unselectedIconColor = MaterialTheme.colorScheme.onSecondary
                    )
                )
            }
        }
    }) {
        val feedPosts = viewModel.feedPosts.observeAsState(listOf())

        LazyColumn(
            contentPadding = PaddingValues(
                top = 16.dp, start = 8.dp, end = 8.dp, bottom = it.calculateBottomPadding() + 8.dp
            ),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(items = feedPosts.value, key = { it.id }) { feedPost ->
                val swipeToDismissBoxState = rememberSwipeToDismissBoxState()

                LaunchedEffect(swipeToDismissBoxState.currentValue) {
                    if (swipeToDismissBoxState.currentValue == SwipeToDismissBoxValue.EndToStart) {
                        viewModel.removeFeedPost(feedPost)
                    }
                }

                SwipeToDismissBox(
                    modifier = Modifier.animateItemPlacement(),
                    state = swipeToDismissBoxState,
                    backgroundContent = {
                        if (swipeToDismissBoxState.dismissDirection == SwipeToDismissBoxValue.StartToEnd) {

                        } else if (swipeToDismissBoxState.dismissDirection == SwipeToDismissBoxValue.EndToStart) {

                        }
                    },
                    enableDismissFromEndToStart = true,
                    enableDismissFromStartToEnd = false,
                    content = {
                        PostCard(
                            feedPost = feedPost,
                            onLikeClickListener = { statisticItem ->
                                viewModel.updateCount(feedPost, statisticItem)
                            },
                            onShareClickListener = { statisticItem ->
                                viewModel.updateCount(feedPost, statisticItem)
                            },
                            onCommentClickListener = { statisticItem ->
                                viewModel.updateCount(feedPost, statisticItem)
                            }
                        )
                    }
                )
            }
        }
    }
}
