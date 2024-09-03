package com.example.vkapp.presentation.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.vkapp.domain.entity.FeedPost
import com.example.vkapp.presentation.home.newsFeed.NewsFeedScreenState
import com.example.vkapp.presentation.home.newsFeed.NewsFeedViewModel
import com.example.vkapp.presentation.home.newsFeed.NewsFeedViewModelFactory
import com.example.vkapp.presentation.home.newsFeed.PostCard
import com.example.vkapp.presentation.home.stories.AddStory
import com.example.vkapp.presentation.home.stories.StoryIcon
import com.vk.id.VKIDUser

@Composable
fun HomeScreen(
    paddingValues: PaddingValues,
    onCommentClickListener: (FeedPost) -> Unit,
    onLinkClickListener: (String) -> Unit,
    user: VKIDUser?
) {
    val newsFeedViewModel: NewsFeedViewModel = viewModel(
        factory = NewsFeedViewModelFactory()
    )
    val newsFeedScreenState = newsFeedViewModel.screenState.collectAsState(NewsFeedScreenState.Initial)

    HomeScreenContent(
        paddingValues = paddingValues,
        newsFeedScreenState = newsFeedScreenState,
        newsFeedViewModel = newsFeedViewModel,
        onCommentClickListener = onCommentClickListener,
        onLinkClickListener= onLinkClickListener,
        user= user,
    )
}

@Composable
fun HomeScreenContent(
    paddingValues: PaddingValues,
    newsFeedScreenState: State<NewsFeedScreenState>,
    newsFeedViewModel: NewsFeedViewModel,
    onCommentClickListener: (FeedPost) -> Unit,
    onLinkClickListener: (String) -> Unit,
    user: VKIDUser?
) {
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
                        if (user != null) {
                            item {
                                AddStory(user = user, itemSize = itemSize)
                            }
                        }

                        items(10) {
                            StoryIcon(itemSize = itemSize)
                            Spacer(modifier = Modifier.width(6.dp))
                        }
                    }
                }
            }
        }
        when (val currentState = newsFeedScreenState.value) {
            is NewsFeedScreenState.Posts -> {
                items(items = currentState.posts, key = { it.id }) { feedPost ->
                    PostCard(
                        feedPost = feedPost,
                        onLikeClickListener = { _ ->
                            newsFeedViewModel.changeLikeStatus(feedPost)
                        },
                        onShareClickListener = { statisticItem ->

                        },
                        onCommentClickListener = {
                            onCommentClickListener(feedPost)
                        },
                        onLinkClickListener = onLinkClickListener
                    )
                }

                item {
                    if (currentState.nextDataIsLoading) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight()
                                .padding(12.dp),
                            contentAlignment = Alignment.Center,
                        ) {
                            CircularProgressIndicator(color = Color.Gray)
                        }
                    } else {
                        SideEffect {
                            newsFeedViewModel.loadNextRecommendations()
                        }
                    }
                }
            }

            is NewsFeedScreenState.Initial -> {

            }

            is NewsFeedScreenState.Loading -> {
                item {
                    Box(
                        modifier = Modifier
                            .fillParentMaxSize()
                            .padding(paddingValues),
                        contentAlignment = Alignment.Center,
                    ) {
                        CircularProgressIndicator(
                            color = Color.Gray,
                            modifier = Modifier.size(48.dp)
                        )
                    }
                }
            }

            is NewsFeedScreenState.Error -> {
                item {
                    Box(
                        modifier = Modifier
                            .fillParentMaxSize()
                            .padding(paddingValues),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(text = currentState.error)
                    }
                }
            }
        }
    }
}