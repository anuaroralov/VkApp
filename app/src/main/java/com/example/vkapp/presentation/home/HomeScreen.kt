package com.example.vkapp.presentation.home

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.vkapp.domain.entity.FeedPost
import com.example.vkapp.presentation.ViewModelFactory
import com.example.vkapp.presentation.home.newsFeed.NewsFeedScreenState
import com.example.vkapp.presentation.home.newsFeed.NewsFeedViewModel
import com.example.vkapp.presentation.home.newsFeed.PostCard
import com.example.vkapp.presentation.home.stories.AddStory
import com.example.vkapp.presentation.home.stories.StoriesScreenState
import com.example.vkapp.presentation.home.stories.StoriesViewModel
import com.example.vkapp.presentation.home.stories.StoryIcon
import com.vk.id.VKIDUser

@Composable
fun HomeScreen(
    viewModelFactory: ViewModelFactory,
    paddingValues: PaddingValues,
    onCommentClickListener: (FeedPost) -> Unit,
    onLinkClickListener: (String) -> Unit,
    user: VKIDUser?
) {
    val newsFeedViewModel: NewsFeedViewModel = viewModel(factory = viewModelFactory)
    val newsFeedScreenState =
        newsFeedViewModel.screenState.collectAsState(NewsFeedScreenState.Initial)

    val storiesViewModel: StoriesViewModel = viewModel(factory = viewModelFactory)
    val storiesScreenState = storiesViewModel.state.collectAsState(StoriesScreenState.Initial)

    val errorState = newsFeedViewModel.errorState.collectAsState(null)

    HomeScreenContent(
        paddingValues = paddingValues,
        newsFeedScreenState = newsFeedScreenState,
        storiesScreenState = storiesScreenState,
        errorState = errorState,
        newsFeedViewModel = newsFeedViewModel,
        onCommentClickListener = onCommentClickListener,
        onLinkClickListener = onLinkClickListener,
        user = user,
    )
}

@Composable
fun HomeScreenContent(
    paddingValues: PaddingValues,
    newsFeedScreenState: State<NewsFeedScreenState>,
    storiesScreenState: State<StoriesScreenState>,
    errorState: State<String?>,
    newsFeedViewModel: NewsFeedViewModel,
    onCommentClickListener: (FeedPost) -> Unit,
    onLinkClickListener: (String) -> Unit,
    user: VKIDUser?
) {
    val context = LocalContext.current
    val error = errorState.value
    if (!error.isNullOrBlank()) {
        LaunchedEffect(errorState) {
            Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
            newsFeedViewModel.clearError()
        }
    }

    LazyColumn(
        contentPadding = PaddingValues(
            top = 0.dp,
            start = 0.dp,
            end = 0.dp,
            bottom = paddingValues.calculateBottomPadding() + 8.dp
        ), verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        when (val currentState = storiesScreenState.value) {
            StoriesScreenState.Initial -> {}
            StoriesScreenState.Loading -> {}
            StoriesScreenState.Error -> {}
            is StoriesScreenState.Stories -> item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surface),
                    shape = RoundedCornerShape(0.dp, 0.dp, 12.dp, 12.dp),
                ) {
                    LazyRow {
                        if (user != null) {
                            item {
                                AddStory(user = user) {}
                            }
                        }
                        items(currentState.stories, key = { it.id }) { story ->
                            StoryIcon(story = story)
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

                if (currentState.errorMessage == null) {
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
                } else {
                    item {
                        Text(text = currentState.errorMessage)
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
                            color = Color.Gray, modifier = Modifier.size(48.dp)
                        )
                    }
                }
            }

            is NewsFeedScreenState.Error -> {
                Log.d("HomeScreenContent", "error")
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