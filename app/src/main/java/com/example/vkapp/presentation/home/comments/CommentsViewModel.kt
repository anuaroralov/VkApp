package com.example.vkapp.presentation.home.comments

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vkapp.data.repository.NewsFeedRepositoryImpl
import com.example.vkapp.domain.entity.FeedPost
import com.example.vkapp.domain.entity.PostComment
import com.example.vkapp.domain.useCase.GetCommentsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CommentsViewModel(
    feedPost: FeedPost
) : ViewModel() {

    private val repository = NewsFeedRepositoryImpl()

    private val getCommentsUseCase = GetCommentsUseCase(repository)

    private val _screenState = MutableStateFlow<CommentsScreenState>(CommentsScreenState.Initial)
    val screenState: StateFlow<CommentsScreenState> = _screenState.asStateFlow()

    init {
        loadComments(feedPost)
    }

    private fun loadComments(feedPost: FeedPost) {
        viewModelScope.launch {
            _screenState.value = CommentsScreenState.Loading
            getCommentsUseCase(feedPost)
                .collect { comments ->
                    _screenState.value = CommentsScreenState.Comments(
                        feedPost = feedPost,
                        comments = comments
                    )
                }
        }
    }

    fun loadNextComments(feedPost: FeedPost) {
        val currentState = _screenState.value as? CommentsScreenState.Comments ?: return
        if (currentState.nextDataIsLoading || !currentState.hasMoreComments) return

        viewModelScope.launch {
            _screenState.value = currentState.copy(nextDataIsLoading = true)
            getCommentsUseCase(feedPost, currentState.offset)
                .collect { newComments ->
                    val updatedComments = currentState.comments + newComments
                    val hasMoreComments = newComments.isNotEmpty()

                    _screenState.value = currentState.copy(
                        comments = updatedComments,
                        nextDataIsLoading = false,
                        hasMoreComments = hasMoreComments,
                        offset = currentState.offset + OFFSET
                    )
                }
        }
    }

    fun loadReplies(comment: PostComment, feedPost: FeedPost) {
        viewModelScope.launch {
            val currentState = _screenState.value as? CommentsScreenState.Comments ?: return@launch
            val initialComment = comment.copy(nextDataIsLoading = true)
            val updatedComments = currentState.comments.map {
                if (it.id == comment.id) initialComment else it
            }

            _screenState.value = currentState.copy(comments = updatedComments)

            getCommentsUseCase(feedPost, commentId = comment.id, offset = comment.repliesOffset)
                .collect { newReplies ->
                    val updatedComment = comment.copy(
                        replies = comment.replies?.copy(items = comment.replies.items + newReplies),
                        repliesOffset = comment.repliesOffset + OFFSET,
                        nextDataIsLoading = false
                    )
                    val finalComments = currentState.comments.map {
                        if (it.id == comment.id) updatedComment else it
                    }

                    _screenState.value = currentState.copy(comments = finalComments)
                }
        }
    }

    companion object {
        const val OFFSET = 20
    }
}

