package com.example.vkapp.ui.theme

import com.example.vkapp.domain.FeedPost
import com.example.vkapp.domain.PostComment

sealed class CommentsScreenState {

    object Initial : CommentsScreenState()

    data class Comments(
        val feedPost: FeedPost,
        val comments: List<PostComment>
    ) : CommentsScreenState()
}