package com.example.vkapp.presentation.home.newsFeed

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vkapp.domain.entity.FeedPost
import com.example.vkapp.domain.entity.NewsFeedResult
import com.example.vkapp.domain.useCase.ChangeLikeStatusUseCase
import com.example.vkapp.domain.useCase.GetRecommendationsUseCase
import com.example.vkapp.domain.useCase.LoadNextFeedPosts
import com.example.vkapp.presentation.utils.mergeWith
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

class NewsFeedViewModel @Inject constructor(
    private val getRecommendationsUseCase: GetRecommendationsUseCase,
    private val loadNextFeedPosts: LoadNextFeedPosts,
    private val changeLikeStatusUseCase: ChangeLikeStatusUseCase
) : ViewModel() {

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Log.e("NewsFeedViewModel", "Exception caught: ${throwable.message}", throwable)
    }

    private val recommendationsFlow = getRecommendationsUseCase()

    private val loadNextDataFlow = MutableSharedFlow<NewsFeedScreenState>()

    val screenState = recommendationsFlow.map { result ->
        when (result) {
            is NewsFeedResult.Success -> {
                if (result.posts.isNotEmpty()) {
                    NewsFeedScreenState.Posts(posts = result.posts)
                } else {
                    NewsFeedScreenState.Initial
                }
            }

            is NewsFeedResult.Error -> NewsFeedScreenState.Error(
                "Failed to load posts " + "\n {${result.exception.message}}"
            )

            is NewsFeedResult.Loading -> NewsFeedScreenState.Loading
        }
    }.mergeWith(loadNextDataFlow)
        .stateIn(viewModelScope, SharingStarted.Lazily, NewsFeedScreenState.Initial)


    fun loadNextRecommendations() {
        viewModelScope.launch {
            loadNextDataFlow.emit(
                NewsFeedScreenState.Posts(
                    posts = (recommendationsFlow.value as NewsFeedResult.Success).posts,
                    nextDataIsLoading = true
                )
            )
            loadNextFeedPosts()
        }
    }

    fun changeLikeStatus(feedPost: FeedPost) {
        viewModelScope.launch(exceptionHandler) {
            changeLikeStatusUseCase(feedPost)
        }
    }
}
