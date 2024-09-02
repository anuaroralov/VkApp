package com.example.vkapp.presentation.home.newsFeed

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vkapp.data.repository.NewsFeedRepository
import com.example.vkapp.domain.FeedPost
import com.example.vkapp.domain.NewsFeedResult
import com.example.vkapp.presentation.utils.mergeWith
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class NewsFeedViewModel(private val repository: NewsFeedRepository) : ViewModel() {

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Log.e("NewsFeedViewModel", "Exception caught: ${throwable.message}", throwable)
    }

    private val recommendationsFlow = repository.recommendations

    private val actionFlow = MutableSharedFlow<NewsFeedAction>()

    val screenState = recommendationsFlow
        .map { result ->
            when (result) {
                is NewsFeedResult.Success -> {
                    if (result.posts.isNotEmpty()) {
                        NewsFeedScreenState.Posts(posts = result.posts)
                    } else {
                        NewsFeedScreenState.Initial
                    }
                }
                is NewsFeedResult.Error -> NewsFeedScreenState.Error("Failed to load posts \n {${result.exception.message}}")
                is NewsFeedResult.Loading -> NewsFeedScreenState.Loading
            }
        }
        .mergeWith(actionFlow.flatMapLatest { action ->
            when (action) {
                is NewsFeedAction.LoadNextRecommendations -> {
                    flow {
                        emit(
                            NewsFeedScreenState.Posts(
                                posts = (recommendationsFlow.value as? NewsFeedResult.Success)?.posts.orEmpty(),
                                nextDataIsLoading = true
                            )
                        )
                        repository.loadNextData()
                    }
                }
                is NewsFeedAction.ChangeLikeStatus -> {
                    flow {
                        repository.changeLikeStatus(action.feedPost)
                        emit(
                            NewsFeedScreenState.Posts(
                                posts = (recommendationsFlow.value as? NewsFeedResult.Success)?.posts.orEmpty()
                            )
                        )
                    }
                }
            }
        })
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = NewsFeedScreenState.Initial
        )

    fun loadNextRecommendations() {
        viewModelScope.launch(exceptionHandler) {
            actionFlow.emit(NewsFeedAction.LoadNextRecommendations)
        }
    }

    fun changeLikeStatus(feedPost: FeedPost) {
        viewModelScope.launch(exceptionHandler) {
            actionFlow.emit(NewsFeedAction.ChangeLikeStatus(feedPost))
        }
    }
}
