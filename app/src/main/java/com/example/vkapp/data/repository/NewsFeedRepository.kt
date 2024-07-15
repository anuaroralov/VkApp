package com.example.vkapp.data.repository

import android.util.Log
import com.example.vkapp.data.mapper.mapResponseToComments
import com.example.vkapp.data.mapper.mapResponseToPosts
import com.example.vkapp.data.network.ApiFactory.apiService
import com.example.vkapp.domain.FeedPost
import com.example.vkapp.domain.PostComment
import com.example.vkapp.domain.StatisticItem
import com.example.vkapp.domain.StatisticType
import com.vk.id.VKID

class NewsFeedRepository {

    private val _feedPosts = mutableListOf<FeedPost>()
    val feedPosts: List<FeedPost>
        get() = _feedPosts.toList()

    private val _comments = mutableListOf<PostComment>()
    val comments: List<PostComment>
        get() = _comments.toList()

    private var nextFromPosts: String? = null
    private var nextFromComments: Int = 0

    suspend fun loadRecommendations(): List<FeedPost> {
        val token = VKID.instance.accessToken?.token ?: throw IllegalStateException("Token is null")
        val startFrom = nextFromPosts

        if (startFrom == null && feedPosts.isNotEmpty()) return feedPosts

        val response = try {
            if (startFrom == null) {
                apiService.loadRecommendations(token)
            } else {
                Log.d("NewsFeedRepository", "loadRecommendations: $startFrom")
                apiService.loadRecommendations(token, startFrom)
            }
        } catch (e: Exception) {
            Log.e("NewsFeedRepository", "Failed to load recommendations", e)
            return emptyList()
        }

        nextFromPosts = response.newsFeedContent.nextFrom

        val posts = response.mapResponseToPosts { ownerId, videoId ->
            try {
                val videoResponse = apiService.getVideo(
                    accessToken = token,
                    videos = ownerId + "_" + videoId
                )
                videoResponse.response?.videoUrls?.lastOrNull()?.videoUrl ?: ""
            } catch (e: Exception) {
                Log.e("NewsFeedRepository", "Failed to get video URL for $ownerId$videoId", e)
                ""
            }
        }

        _feedPosts.addAll(posts)
        return feedPosts
    }

    suspend fun changeLikeStatus(feedPost: FeedPost) {
        val token = VKID.instance.accessToken?.token ?: throw IllegalStateException("Token is null")
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

    suspend fun getComments(feedPost: FeedPost): List<PostComment> {
        val startFrom = nextFromComments
        val token = VKID.instance.accessToken?.token ?: throw IllegalStateException("Token is null")

        if (startFrom == 0 && comments.isNotEmpty()) return comments

        val response = if (startFrom == 0) {
            apiService.getComments(
                accessToken = token,
                ownerId = feedPost.communityId,
                postId = feedPost.id,
            )
        } else {
            Log.d("NewsFeedRepository", "loadRecommendations: $startFrom")
            apiService.getComments(
                accessToken = token,
                ownerId = feedPost.communityId,
                postId = feedPost.id,
                offset = startFrom
            )
        }
        val comms = response.mapResponseToComments()
        nextFromPosts += comms.size
        _comments.addAll(comms)
        return comments
    }
}
