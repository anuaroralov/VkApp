package com.example.vkapp.presentation.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.vkapp.domain.FeedPost
import com.example.vkapp.presentation.newsFeed.FeedPostItem
import com.example.vkapp.presentation.newsFeed.NewsFeedScreenState
import com.example.vkapp.presentation.newsFeed.NewsFeedViewModel
import com.example.vkapp.presentation.stories.StoryIcon

@Composable
fun HomeScreen(
    paddingValues: PaddingValues,
    onCommentClickListener: (FeedPost) -> Unit
) {
    val viewModel: NewsFeedViewModel = viewModel()
    val screenState = viewModel.screenState.observeAsState(NewsFeedScreenState.Initial)
    LazyColumn(
        contentPadding = PaddingValues(
            top = 0.dp,
            start = 0.dp,
            end = 0.dp,
            bottom = paddingValues.calculateBottomPadding() + 8.dp
        ),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            BoxWithConstraints {
                val maxWidth = constraints.maxWidth
                val itemSize = (maxWidth / 12).dp

                Card(
                    colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surface),
                    shape = RoundedCornerShape(0.dp, 0.dp, 12.dp, 12.dp)
                ) {
                    LazyRow {
                        items(10) {
                            StoryIcon(itemSize = itemSize)
                            Spacer(modifier = Modifier.width(6.dp))
                        }
                    }
                }
            }
        }
        when (val currentState = screenState.value) {
            is NewsFeedScreenState.Posts -> {
                items(items = currentState.posts, key = { it.id }) { feedPost ->
                    FeedPostItem(
                        feedPost = feedPost,
                        viewModel = viewModel,
                        onCommentClickListener = onCommentClickListener
                    )
                }
            }

            NewsFeedScreenState.Initial -> {
                // Handle the initial state
            }
        }
    }
}