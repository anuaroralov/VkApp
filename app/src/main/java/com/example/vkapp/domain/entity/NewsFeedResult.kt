package com.example.vkapp.domain.entity

sealed class NewsFeedResult {
    data class Error(val exception: Throwable) : NewsFeedResult()
    data class Success(val posts: List<FeedPost>) : NewsFeedResult()
    data object Loading : NewsFeedResult()
}