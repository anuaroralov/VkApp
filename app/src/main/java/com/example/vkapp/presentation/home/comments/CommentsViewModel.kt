package com.example.vkapp.presentation.home.comments

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vkapp.data.repository.NewsFeedRepository
import com.example.vkapp.domain.FeedPost
import com.example.vkapp.domain.PostComment
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class CommentsViewModel(
    feedPost: FeedPost
) : ViewModel() {

    private val repository = NewsFeedRepository()

    private val _screenState = MutableLiveData<CommentsScreenState>(CommentsScreenState.Initial)
    val screenState: LiveData<CommentsScreenState> = _screenState

    private val _nextDataIsLoading=MutableLiveData(false)
    val nextDataIsLoading:LiveData<Boolean> = _nextDataIsLoading

    private val _hasMoreComments = MutableLiveData<Boolean>(true)
    val hasMoreComments: LiveData<Boolean> = _hasMoreComments

    private var offset = OFFSET

    init {
        loadComments(feedPost)
    }

    private fun loadComments(feedPost: FeedPost) {
        viewModelScope.launch {
            _screenState.value = CommentsScreenState.Loading
            val newComments = repository.getComments(feedPost)
            _screenState.value = CommentsScreenState.Comments(
                feedPost = feedPost,
                comments = newComments
            )
        }
    }

    fun loadNextComments(feedPost: FeedPost) {
        if (_screenState.value == CommentsScreenState.Loading || _hasMoreComments.value == false) return

        viewModelScope.launch {
            _nextDataIsLoading.value=true
            val currentComments = (_screenState.value as CommentsScreenState.Comments).comments
            val newComments = repository.getComments(feedPost, offset)
            val updatedComments = currentComments + newComments // Concatenate the lists
            _hasMoreComments.value = newComments.isNotEmpty()
            _nextDataIsLoading.value=false
            _screenState.value = CommentsScreenState.Comments(
                feedPost = feedPost,
                comments = updatedComments,
            )
            offset += OFFSET
        }
    }
    companion object{
        const val OFFSET=20
    }
}


