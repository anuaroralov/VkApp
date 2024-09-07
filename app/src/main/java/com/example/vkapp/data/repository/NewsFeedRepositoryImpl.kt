package com.example.vkapp.data.repository

import android.util.Log
import com.example.vkapp.data.mapper.mapResponseToComments
import com.example.vkapp.data.mapper.mapResponseToPosts
import com.example.vkapp.data.mapper.mapResponseToStories
import com.example.vkapp.data.network.ApiService
import com.example.vkapp.domain.NewsFeedRepository
import com.example.vkapp.domain.entity.CommentsResult
import com.example.vkapp.domain.entity.FeedPost
import com.example.vkapp.domain.entity.NewsFeedResult
import com.example.vkapp.domain.entity.StatisticItem
import com.example.vkapp.domain.entity.StatisticType
import com.example.vkapp.domain.entity.StoriesResult
import com.example.vkapp.presentation.utils.mergeWith
import com.vk.id.VKID
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.retry
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject


class NewsFeedRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
) : NewsFeedRepository {

    private val coroutineScope = CoroutineScope(Dispatchers.Default)

    private val nextDataNeededEvents = MutableSharedFlow<Unit>(replay = 1)
    private val refreshedListFlow = MutableSharedFlow<NewsFeedResult>()

    private val _feedPosts = mutableListOf<FeedPost>()
    private val feedPosts: List<FeedPost>
        get() = _feedPosts.toList()

    private var nextFrom: String? = null

    private val recommendations: StateFlow<NewsFeedResult> = flow {
        nextDataNeededEvents.emit(Unit)
        nextDataNeededEvents.collect {
            val token =
                VKID.instance.accessToken?.token ?: throw IllegalStateException("Token is null")
            val startFrom = nextFrom

            if (startFrom == null && feedPosts.isNotEmpty()) {
                emit(feedPosts)
            } else {
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
                        Log.e(
                            "NewsFeedRepository", "Failed to get video URL for $ownerId$videoId", e
                        )
                        ""
                    }
                }

                _feedPosts.addAll(posts)
                emit(feedPosts)
            }
        }
    }.map { NewsFeedResult.Success(posts = it) as NewsFeedResult }.retry(3) {
        delay(RETRY_TIMEOUT_MILLIS)
        true
    }.catch { e ->
        if (_feedPosts.isEmpty()) emit(NewsFeedResult.Error(e)) else emit(
            NewsFeedResult.Success(posts = feedPosts, errorMessage = e)
        )
    }
        .mergeWith(refreshedListFlow).stateIn(
            scope = coroutineScope,
            started = SharingStarted.Lazily,
            initialValue = NewsFeedResult.Loading
        )

    override fun getRecommendations(): StateFlow<NewsFeedResult> {
        return recommendations
    }

    override suspend fun loadNextFeedPosts() {
        nextDataNeededEvents.emit(Unit)
    }

    override suspend fun changeLikeStatus(feedPost: FeedPost) {
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
            refreshedListFlow.emit(NewsFeedResult.Success(feedPosts))
        }
    }

    override fun getComments(
        feedPost: FeedPost, offset: Int, commentId: Long?
    ): Flow<CommentsResult> = flow {
        emit(CommentsResult.Loading)
        val token = VKID.instance.accessToken?.token
            ?: throw IllegalStateException("Token is null")

        val comments = apiService.getComments(
            accessToken = token,
            ownerId = feedPost.communityId,
            postId = feedPost.id,
            offset = offset,
            commentId = commentId
        ).mapResponseToComments()
        emit(CommentsResult.Success(comments))
    }.catch { e -> emit(CommentsResult.Error(e)) }


    override fun getStories(): Flow<StoriesResult> = flow {
        emit(StoriesResult.Loading)
        val token = VKID.instance.accessToken?.token ?: throw IllegalStateException("Token is null")
        val stories = apiService.getStories(token).mapResponseToStories()
        emit(StoriesResult.Success(stories))
    }.catch { e -> emit(StoriesResult.Error(e)) }

    companion object {
        private const val RETRY_TIMEOUT_MILLIS = 3000L
    }
}