package com.example.vkapp.domain

import com.example.vkapp.domain.entity.FeedPost
import com.example.vkapp.domain.entity.NewsFeedResult
import com.example.vkapp.domain.entity.PostComment
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface NewsFeedRepository {

    fun getRecommendations(): StateFlow<NewsFeedResult>

    fun getComments(
        feedPost: FeedPost,
        offset: Int = 0,
        commentId: Long? = null
    ): Flow<List<PostComment>>

    suspend fun loadNextFeedPosts()

    suspend fun changeLikeStatus(feedPost: FeedPost)


}