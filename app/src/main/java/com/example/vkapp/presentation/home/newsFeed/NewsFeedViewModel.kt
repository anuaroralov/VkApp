package com.example.vkapp.presentation.home.newsFeed

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vkapp.data.repository.NewsFeedRepository
import com.example.vkapp.domain.FeedPost
import com.example.vkapp.domain.StatisticItem
import com.example.vkapp.extensions.mergeWith

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class NewsFeedViewModel : ViewModel() {

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Log.e("NewsFeedViewModel", "Exception caught: ${throwable.message}", throwable)
    }

    private val repository: NewsFeedRepository by lazy { NewsFeedRepository() }

    private val recommendationsFlow = repository.recommendations

    private val actionFlow = MutableSharedFlow<NewsFeedAction>()

    val screenState: StateFlow<NewsFeedScreenState> = recommendationsFlow
        .filter { it.isNotEmpty() }
        .map { NewsFeedScreenState.Posts(posts = it) as NewsFeedScreenState }
        .onStart { emit(NewsFeedScreenState.Loading) }
        .mergeWith(actionFlow.flatMapLatest { action ->
            when (action) {
                is NewsFeedAction.LoadNextRecommendations -> {
                    flow {
                        emit(
                            NewsFeedScreenState.Posts(
                                posts = recommendationsFlow.value,
                                nextDataIsLoading = true
                            )
                        )
                        repository.loadNextData()
                    }
                }
                is NewsFeedAction.ChangeLikeStatus -> {
                    flow {
                        repository.changeLikeStatus(action.feedPost)
                        emit(NewsFeedScreenState.Posts(posts = recommendationsFlow.value))
                    }
                }
            }
        })
        .catch { e ->
            Log.e("NewsFeedViewModel", "Error in screenState flow: ${e.message}", e)
            emit(NewsFeedScreenState.Error("Failed to load posts"))
        }
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
