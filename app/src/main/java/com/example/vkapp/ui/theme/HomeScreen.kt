package com.example.vkapp.ui.theme

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.vkapp.MyViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(viewModel: MyViewModel, it: PaddingValues) {
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