package com.example.vkapp.presentation.newsFeed

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.example.vkapp.domain.FeedPost
import androidx.compose.foundation.lazy.LazyItemScope

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun LazyItemScope.FeedPostItem(
    feedPost: FeedPost,
    viewModel: NewsFeedViewModel,
    onCommentClickListener: (FeedPost) -> Unit
) {
    val swipeToDismissBoxState = rememberSwipeToDismissBoxState()

    LaunchedEffect(swipeToDismissBoxState.currentValue) {
        if (swipeToDismissBoxState.currentValue == SwipeToDismissBoxValue.EndToStart) {
            viewModel.remove(feedPost)
        }
    }

    SwipeToDismissBox(
        modifier = Modifier.animateItemPlacement(),
        state = swipeToDismissBoxState,
        backgroundContent = {
            if (swipeToDismissBoxState.dismissDirection == SwipeToDismissBoxValue.StartToEnd) {
                // Handle start to end swipe background
            } else if (swipeToDismissBoxState.dismissDirection == SwipeToDismissBoxValue.EndToStart) {
                // Handle end to start swipe background
            }
        },
        enableDismissFromEndToStart = true,
        enableDismissFromStartToEnd = false,
        content = {
            PostCard(
                feedPost = feedPost,
                onLikeClickListener = { _ ->
                    viewModel.changeLikeStatus(feedPost)
                },
                onShareClickListener = { statisticItem ->
                    viewModel.updateCount(feedPost, statisticItem)
                },
                onCommentClickListener = {
                    onCommentClickListener(feedPost)
                }
            )
        }
    )
}