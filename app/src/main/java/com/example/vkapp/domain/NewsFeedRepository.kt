package com.example.vkapp.domain

import com.example.vkapp.domain.entity.CommentsResult
import com.example.vkapp.domain.entity.FeedPost
import com.example.vkapp.domain.entity.NewsFeedResult
import com.example.vkapp.domain.entity.StoriesResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface NewsFeedRepository {

    fun getRecommendations(): StateFlow<NewsFeedResult>

    fun getStories(): Flow<StoriesResult>

    fun getComments(
        feedPost: FeedPost,
        offset: Int = 0,
        commentId: Long? = null
    ): Flow<CommentsResult>

    suspend fun loadNextFeedPosts()

    suspend fun changeLikeStatus(feedPost: FeedPost)


}