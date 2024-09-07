package com.example.vkapp.presentation.home.newsFeed

import com.example.vkapp.domain.entity.FeedPost

sealed class NewsFeedScreenState {

    data object Initial : NewsFeedScreenState()

    data object Loading : NewsFeedScreenState()

    data class Error(val error: String, val posts: List<FeedPost>? = null) : NewsFeedScreenState()

    data class Posts(
        val posts: List<FeedPost>,
        val nextDataIsLoading: Boolean = false,
        val errorMessage: String? = null
    ) : NewsFeedScreenState()
}