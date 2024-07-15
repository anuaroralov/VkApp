package com.example.vkapp.presentation.home.comments

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vkapp.data.repository.NewsFeedRepository
import com.example.vkapp.domain.FeedPost
import kotlinx.coroutines.launch

class CommentsViewModel(
    feedPost: FeedPost
) : ViewModel() {

    private val repository = NewsFeedRepository()

    private val _screenState = MutableLiveData<CommentsScreenState>(CommentsScreenState.Initial)
    val screenState: LiveData<CommentsScreenState> = _screenState

    init {
        loadComments(feedPost)
    }

    private fun loadComments(feedPost: FeedPost) {
        viewModelScope.launch {
            val comments = repository.getComments(feedPost)
            _screenState.value = CommentsScreenState.Comments(
                feedPost = feedPost,
                comments = comments
            )
        }
    }

    fun loadNextComments(feedPost: FeedPost) {
        _screenState.value = CommentsScreenState.Comments(
            comments = repository.comments,
            feedPost = feedPost,
            nextDataIsLoading = true
        )
        loadComments(feedPost)
    }


//    fun addComment(feedPost: FeedPost, comment: String) {
//        viewModelScope.launch {
//            val newComment = repository.addComment(feedPost, comment)
//            val currentState = _screenState.value
//            if (currentState is CommentsScreenState.Comments) {
//                _screenState.value = currentState.copy(
//                    comments = currentState.comments + newComment
//                )
//            }
//        }
//    }
}
