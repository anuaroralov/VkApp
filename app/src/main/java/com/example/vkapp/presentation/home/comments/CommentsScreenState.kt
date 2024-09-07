package com.example.vkapp.presentation.home.comments

import com.example.vkapp.domain.entity.FeedPost
import com.example.vkapp.domain.entity.PostComment

sealed class CommentsScreenState {

    data object Initial : CommentsScreenState()

    data class Comments(
        val feedPost: FeedPost,
        val comments: List<PostComment>,
        val nextDataIsLoading: Boolean = false,
        val hasMoreComments: Boolean = true,
        val errorMessage: String? = null
    ) : CommentsScreenState()

    data object Loading : CommentsScreenState()

    data class Error(val text: String) : CommentsScreenState()

}
