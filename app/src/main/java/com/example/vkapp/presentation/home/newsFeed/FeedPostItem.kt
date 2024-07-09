package com.example.vkapp.presentation.home.newsFeed

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import com.example.vkapp.domain.FeedPost

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun LazyItemScope.FeedPostItem(
    feedPost: FeedPost,
    viewModel: NewsFeedViewModel,
    onCommentClickListener: (FeedPost) -> Unit,
    onLinkClickListener: (String) -> Unit
) {
//    val swipeToDismissBoxState = rememberSwipeToDismissBoxState()
//
//    LaunchedEffect(swipeToDismissBoxState.currentValue) {
//        if (swipeToDismissBoxState.currentValue == SwipeToDismissBoxValue.EndToStart) {
//            viewModel.remove(feedPost)
//        }
//    }
//
//    SwipeToDismissBox(
//        modifier = Modifier.animateItemPlacement(),
//        state = swipeToDismissBoxState,
//        backgroundContent = {
//            if (swipeToDismissBoxState.dismissDirection == SwipeToDismissBoxValue.StartToEnd) {
//                // Handle start to end swipe background
//            } else if (swipeToDismissBoxState.dismissDirection == SwipeToDismissBoxValue.EndToStart) {
//                // Handle end to start swipe background
//            }
//        },
//        enableDismissFromEndToStart = true,
//        enableDismissFromStartToEnd = false,
//        content = {
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
        },
        onLinkClickListener = onLinkClickListener
    )
}
//    )
//}


