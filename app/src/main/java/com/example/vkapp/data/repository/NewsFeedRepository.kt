package com.example.vkapp.data.repository

import com.example.vkapp.data.mapper.mapResponseToPosts
import com.example.vkapp.data.network.ApiFactory.apiService
import com.example.vkapp.domain.FeedPost
import com.example.vkapp.domain.StatisticItem
import com.example.vkapp.domain.StatisticType
import com.vk.id.VKID

class NewsFeedRepository() {

    private val _feedPosts = mutableListOf<FeedPost>()
    val feedPosts: List<FeedPost>
        get() = _feedPosts.toList()

    suspend fun loadRecommendations(): List<FeedPost> {
        val token=VKID.instance.accessToken?.token?: throw IllegalStateException("Token is null")
        val response = apiService.loadRecommendations(token)
        val posts = response.mapResponseToPosts()
        _feedPosts.addAll(posts)
        return posts
    }

    suspend fun changeLikeStatus(feedPost: FeedPost) {
        val token=VKID.instance.accessToken?.token?: throw IllegalStateException("Token is null")
        val response = if (feedPost.isLiked) {
            apiService.deleteLike(
                token = token,
                ownerId = feedPost.communityId,
                postId = feedPost.id
            )
        } else {
            apiService.addLike(
                token = token,
                ownerId = feedPost.communityId,
                postId = feedPost.id
            )
        }
        val newLikesCount = response.likes.count
        val newStatistics = feedPost.statistics.toMutableList().apply {
            removeIf { it.type == StatisticType.LIKES }
            add(StatisticItem(type = StatisticType.LIKES, newLikesCount))
        }
        val newPost = feedPost.copy(statistics = newStatistics, isLiked = !feedPost.isLiked)
        val postIndex = _feedPosts.indexOf(feedPost)
        _feedPosts[postIndex] = newPost
    }
}