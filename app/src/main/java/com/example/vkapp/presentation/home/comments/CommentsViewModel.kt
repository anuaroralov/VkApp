package com.example.vkapp.presentation.home.comments

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vkapp.data.repository.NewsFeedRepository
import com.example.vkapp.domain.FeedPost
import com.example.vkapp.domain.PostComment
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
            _screenState.value = CommentsScreenState.Loading
            val newComments = repository.getComments(feedPost)
            _screenState.value = CommentsScreenState.Comments(
                feedPost = feedPost,
                comments = newComments
            )
        }
    }

    fun loadNextComments(feedPost: FeedPost) {
        val currentState = _screenState.value as? CommentsScreenState.Comments ?: return
        if (currentState.nextDataIsLoading || !currentState.hasMoreComments) return

        viewModelScope.launch {
            val updatedState = currentState.copy(nextDataIsLoading = true)
            _screenState.value = updatedState

            val newComments = repository.getComments(feedPost, currentState.offset)

            val updatedComments = currentState.comments + newComments

            val hasMoreComments = newComments.isNotEmpty()

            _screenState.value = updatedState.copy(
                comments = updatedComments,
                nextDataIsLoading = false,
                hasMoreComments = hasMoreComments,
                offset = updatedState.offset + OFFSET
            )
        }
    }


    fun loadReplies(comment: PostComment, feedPost: FeedPost) {

        viewModelScope.launch {
            val initialComment = comment.copy(
                nextDataIsLoading = true
            )

            val initialState = _screenState.value as? CommentsScreenState.Comments
            val initialComments =
                initialState?.comments?.map { if (it.id == comment.id) initialComment else it }

            _screenState.value = CommentsScreenState.Comments(
                feedPost = feedPost,
                comments = initialComments ?: initialState?.comments.orEmpty()
            )

            val newReplies = repository.getComments(
                feedPost = feedPost,
                commentId = comment.id,
                offset = comment.repliesOffset
            )

            val updatedComment = comment.copy(
                replies = comment.replies?.copy(items = comment.replies.items+newReplies),
                repliesOffset = comment.repliesOffset+ OFFSET,
                nextDataIsLoading = false
            )

            val currentState = _screenState.value as? CommentsScreenState.Comments
            val updatedComments =
                currentState?.comments?.map { if (it.id == comment.id) updatedComment else it }

            _screenState.value = CommentsScreenState.Comments(
                feedPost = feedPost,
                comments = updatedComments ?: currentState?.comments.orEmpty()
            )

        }
    }

    companion object {
        const val OFFSET = 20
    }
}
