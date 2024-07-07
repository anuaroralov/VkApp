package com.example.vkapp.presentation.newsFeed

import com.example.vkapp.domain.FeedPost

sealed class NewsFeedScreenState {

    data object Initial : NewsFeedScreenState()

    data class Posts(
        val posts: List<FeedPost>,
        val nextDataIsLoading: Boolean = false
    ) : NewsFeedScreenState()
}