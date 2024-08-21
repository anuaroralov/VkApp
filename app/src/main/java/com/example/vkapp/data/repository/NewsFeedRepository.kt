package com.example.vkapp.data.repository

import android.util.Log
import com.example.vkapp.data.mapper.mapResponseToComments
import com.example.vkapp.data.mapper.mapResponseToPosts
import com.example.vkapp.data.network.ApiFactory.apiService
import com.example.vkapp.domain.FeedPost
import com.example.vkapp.domain.PostComment
import com.example.vkapp.domain.StatisticItem
import com.example.vkapp.domain.StatisticType
import com.example.vkapp.extensions.mergeWith
import com.vk.id.VKID
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.retry
import kotlinx.coroutines.flow.stateIn


class NewsFeedRepository {

    private val coroutineScope = CoroutineScope(Dispatchers.Default)

    private val nextDataNeededEvents = MutableSharedFlow<Unit>(replay = 1)
    private val refreshedListFlow = MutableSharedFlow<List<FeedPost>>()

    private val _feedPosts = mutableListOf<FeedPost>()
    private val feedPosts: List<FeedPost>
        get() = _feedPosts.toList()

    private var nextFrom: String? = null

    // Загруженный список постов
    private val loadedListFlow = flow {
        nextDataNeededEvents.emit(Unit)
        nextDataNeededEvents.collect {
            val token = VKID.instance.accessToken?.token ?: throw IllegalStateException("Token is null")
            val startFrom = nextFrom

            if (startFrom == null && feedPosts.isNotEmpty()) {
                emit(feedPosts)
                return@collect
            }

            val response = if (startFrom == null) {
                apiService.loadRecommendations(token)
            } else {
                Log.d("NewsFeedRepository", "loadRecommendations: $startFrom")
                apiService.loadRecommendations(token, startFrom)
            }

            nextFrom = response.newsFeedContent.nextFrom

            val posts = response.mapResponseToPosts { ownerId, videoId ->
                try {
                    val videoResponse = apiService.getVideo(
                        accessToken = token, videos = ownerId + "_" + videoId
                    )
                    videoResponse.response.videoUrls?.lastOrNull()?.videoUrl ?: ""
                } catch (e: Exception) {
                    Log.e("NewsFeedRepository", "Failed to get video URL for $ownerId$videoId", e)
                    ""
                }
            }

            _feedPosts.addAll(posts)
            emit(feedPosts)
        }
    }.retry {
        delay(RETRY_TIMEOUT_MILLIS)
        true
    }

    // Публичный поток с рекомендациями
    val recommendations: StateFlow<List<FeedPost>> =
        loadedListFlow.mergeWith(refreshedListFlow).stateIn(
            scope = coroutineScope, started = SharingStarted.Lazily, initialValue = feedPosts
        )

    // Загрузка следующих данных
    suspend fun loadNextData() {
        nextDataNeededEvents.emit(Unit)
    }

    // Изменение статуса "лайка"
    suspend fun changeLikeStatus(feedPost: FeedPost) {
        val token = VKID.instance.accessToken?.token ?: throw IllegalStateException("Token is null")
        val response = if (feedPost.isLiked) {
            apiService.deleteLike(token, feedPost.communityId, feedPost.id)
        } else {
            apiService.addLike(token, feedPost.communityId, feedPost.id)
        }
        val newLikesCount = response.likes.count
        val newStatistics = feedPost.statistics.toMutableList().apply {
            removeIf { it.type == StatisticType.LIKES }
            add(StatisticItem(StatisticType.LIKES, newLikesCount))
        }
        val newPost = feedPost.copy(statistics = newStatistics, isLiked = !feedPost.isLiked)
        val postIndex = _feedPosts.indexOf(feedPost)
        if (postIndex != -1) {
            _feedPosts[postIndex] = newPost
            refreshedListFlow.emit(feedPosts)
        }
    }

    suspend fun getComments(feedPost: FeedPost, offset: Int = 0): List<PostComment> {
        val token = VKID.instance.accessToken?.token ?: throw IllegalStateException("Token is null")

        return apiService.getComments(
            accessToken = token,
            ownerId = feedPost.communityId,
            postId = feedPost.id,
            offset = offset
        ).mapResponseToComments()
    }

    companion object {
        private const val RETRY_TIMEOUT_MILLIS = 3000L
    }
}