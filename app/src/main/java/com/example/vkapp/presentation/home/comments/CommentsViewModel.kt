package com.example.vkapp.presentation.home.comments

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vkapp.R
import com.example.vkapp.domain.entity.CommentsResult
import com.example.vkapp.domain.entity.FeedPost
import com.example.vkapp.domain.entity.PostComment
import com.example.vkapp.domain.useCase.GetCommentsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class CommentsViewModel @Inject constructor(
    private val feedPost: FeedPost,
    private val getCommentsUseCase: GetCommentsUseCase,
    private val context: Context
) : ViewModel() {

    private val _screenState = MutableStateFlow<CommentsScreenState>(CommentsScreenState.Initial)
    val screenState: StateFlow<CommentsScreenState> = _screenState.asStateFlow()

    private val _errorState = MutableStateFlow<String?>(null)
    val errorState: StateFlow<String?> = _errorState.asStateFlow()

    private var _comments = mutableListOf<PostComment>()
    private val comments: List<PostComment>
        get() = _comments.toList()

    private var offset = 0

    init {
        loadComments(feedPost)
    }

    fun loadComments(feedPost: FeedPost) {
        viewModelScope.launch {
            getCommentsUseCase(feedPost, offset = offset)
                .collect { result ->
                    when (result) {
                        is CommentsResult.Loading -> {
                            if (comments.isEmpty()) {
                                _screenState.value = CommentsScreenState.Loading
                            } else {
                                _screenState.value =
                                    CommentsScreenState.Comments(feedPost, comments, true)
                            }
                        }

                        is CommentsResult.Success -> {
                            if (result.comments.isEmpty()) {
                                _screenState.value = CommentsScreenState.Comments(
                                    feedPost = feedPost,
                                    comments = comments,
                                    nextDataIsLoading = false,
                                    hasMoreComments = false
                                )
                            } else {
                                _comments.addAll(result.comments)
                                _screenState.value = CommentsScreenState.Comments(
                                    feedPost = feedPost,
                                    comments = comments,
                                    nextDataIsLoading = false,
                                )
                                offset += OFFSET
                            }
                        }

                        is CommentsResult.Error -> {
                            if (comments.isEmpty()) {
                                _screenState.value = CommentsScreenState.Error(
                                    context.getString(
                                        R.string.error_message,
                                        result.exception.message
                                    )
                                )
                            } else {
                                _screenState.value = CommentsScreenState.Comments(
                                    feedPost = feedPost,
                                    comments = comments,
                                    errorMessage = context.getString(
                                        R.string.error_message,
                                        result.exception.message
                                    )
                                )
                            }

                        }
                    }
                }
        }
    }

    fun loadReplies(comment: PostComment, feedPost: FeedPost) {

        viewModelScope.launch {
            getCommentsUseCase(feedPost, commentId = comment.id, offset = comment.repliesOffset)
                .collect { result ->
                    when (result) {
                        is CommentsResult.Loading -> {
                            val initialComment = comment.copy(nextDataIsLoading = true)
                            val updatedComments = comments.map {
                                if (it.id == comment.id) initialComment else it
                            }
                            _comments = updatedComments.toMutableList()

                            _screenState.value = CommentsScreenState.Comments(
                                comments = comments,
                                feedPost = feedPost
                            )
                        }

                        is CommentsResult.Success -> {
                            if (result.comments.isEmpty()) {
                                val initialComment = comment.copy(nextDataIsLoading = false)
                                val updatedComments = comments.map {
                                    if (it.id == comment.id) initialComment else it
                                }
                                _comments = updatedComments.toMutableList()

                                _screenState.value = CommentsScreenState.Comments(
                                    comments = comments,
                                    feedPost = feedPost
                                )
                            } else {
                                val updatedComment = comment.copy(
                                    replies = comment.replies?.copy(items = comment.replies.items + result.comments),
                                    repliesOffset = comment.repliesOffset + OFFSET,
                                    nextDataIsLoading = false
                                )
                                val finalComments = comments.map {
                                    if (it.id == comment.id) updatedComment else it
                                }
                                _comments = finalComments.toMutableList()

                                _screenState.value = CommentsScreenState.Comments(
                                    comments = comments,
                                    feedPost = feedPost
                                )
                            }

                        }

                        is CommentsResult.Error -> {
                            _errorState.value =
                                context.getString(R.string.error_message, result.exception.message)

                            val initialComment = comment.copy(nextDataIsLoading = false)
                            val updatedComments = comments.map {
                                if (it.id == comment.id) initialComment else it
                            }
                            _comments = updatedComments.toMutableList()

                            _screenState.value = CommentsScreenState.Comments(
                                comments = comments,
                                feedPost = feedPost
                            )

                        }
                    }
                }
        }
    }

    fun clearError() {
        _errorState.value = null
    }

    companion object {
        const val OFFSET = 20
    }
}






