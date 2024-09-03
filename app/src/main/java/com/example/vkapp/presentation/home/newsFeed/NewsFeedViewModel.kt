package com.example.vkapp.presentation.home.newsFeed

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vkapp.data.repository.NewsFeedRepositoryImpl
import com.example.vkapp.domain.entity.FeedPost
import com.example.vkapp.domain.entity.NewsFeedResult
import com.example.vkapp.domain.useCase.ChangeLikeStatusUseCase
import com.example.vkapp.domain.useCase.GetRecommendationsUseCase
import com.example.vkapp.domain.useCase.LoadNextFeedPosts
import com.example.vkapp.presentation.utils.mergeWith
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class NewsFeedViewModel() : ViewModel() {

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Log.e("NewsFeedViewModel", "Exception caught: ${throwable.message}", throwable)
    }

    private val repository= NewsFeedRepositoryImpl()

    private val getRecommendationsUseCase = GetRecommendationsUseCase(repository)
    private val loadNextFeedPosts = LoadNextFeedPosts(repository)
    private val changeLikeStatusUseCase = ChangeLikeStatusUseCase(repository)

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
                "Failed to load posts " + "\n {${result.exception.message}}")

            is NewsFeedResult.Loading -> NewsFeedScreenState.Loading
        }
    }.mergeWith(loadNextDataFlow)


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
