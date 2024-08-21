package com.example.vkapp.presentation.home.newsFeed

import com.example.vkapp.domain.FeedPost

sealed class NewsFeedAction {
    data object LoadNextRecommendations : NewsFeedAction()
    data class ChangeLikeStatus(val feedPost: FeedPost) : NewsFeedAction()
}