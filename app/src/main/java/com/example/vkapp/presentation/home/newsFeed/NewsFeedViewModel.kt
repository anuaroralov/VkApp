package com.example.vkapp.presentation.home.newsFeed


import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vkapp.R
import com.example.vkapp.domain.entity.FeedPost
import com.example.vkapp.domain.entity.NewsFeedResult
import com.example.vkapp.domain.useCase.ChangeLikeStatusUseCase
import com.example.vkapp.domain.useCase.GetRecommendationsUseCase
import com.example.vkapp.domain.useCase.LoadNextFeedPostsUseCase
import com.example.vkapp.presentation.utils.mergeWith
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

class NewsFeedViewModel @Inject constructor(
    private val getRecommendationsUseCase: GetRecommendationsUseCase,
    private val loadNextFeedPostsUseCase: LoadNextFeedPostsUseCase,
    private val changeLikeStatusUseCase: ChangeLikeStatusUseCase,
    private val context: Context
) : ViewModel() {

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        _errorState.value =
            context.getString(R.string.error_message, throwable.message)
    }

    private val recommendationsFlow = getRecommendationsUseCase()

    private val loadNextDataFlow = MutableSharedFlow<NewsFeedScreenState>()

    private val _errorState = MutableStateFlow<String?>(null)
    val errorState: StateFlow<String?> = _errorState.asStateFlow()

    val screenState: StateFlow<NewsFeedScreenState> = recommendationsFlow.map { result ->
        when (result) {
            is NewsFeedResult.Success -> {
                if (result.errorMessage == null) {
                    NewsFeedScreenState.Posts(posts = result.posts)
                } else {
                    NewsFeedScreenState.Posts(
                        posts = result.posts,
                        errorMessage = context.getString(
                            R.string.error_message,
                            result.errorMessage
                        )
                    )
                }
            }

            is NewsFeedResult.Error -> NewsFeedScreenState.Error(
                context.getString(R.string.error_message, result.exception.message)
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
            loadNextFeedPostsUseCase()
        }
    }

    fun changeLikeStatus(feedPost: FeedPost) {
        viewModelScope.launch(exceptionHandler) {
            changeLikeStatusUseCase(feedPost)
        }
    }

    fun clearError() {
        _errorState.value = null
    }
}
