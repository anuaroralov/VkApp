package com.example.vkapp.data.network

import com.example.vkapp.data.model.feedPost.CommentsResponseDto
import com.example.vkapp.data.model.feedPost.LikesCountResponseDto
import com.example.vkapp.data.model.feedPost.NewsFeedResponseDto
import com.example.vkapp.data.model.feedPost.VideoUrlResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("newsfeed.get?v=5.199&filters=post")
    suspend fun loadRecommendations(
        @Query("access_token") token: String
    ): NewsFeedResponseDto

    @GET("newsfeed.get?v=5.199&filters=post")
    suspend fun loadRecommendations(
        @Query("access_token") token: String, @Query("start_from") startFrom: String
    ): NewsFeedResponseDto

    @GET("video.get?v=5.199")
    suspend fun getVideo(
        @Query("access_token") accessToken: String, @Query("videos") videos: String
    ): VideoUrlResponseDto

    @GET("likes.add?v=5.199&type=post")
    suspend fun addLike(
        @Query("access_token") token: String,
        @Query("owner_id") ownerId: Long,
        @Query("item_id") postId: Long
    ): LikesCountResponseDto

    @GET("likes.delete?v=5.199&type=post")
    suspend fun deleteLike(
        @Query("access_token") token: String,
        @Query("owner_id") ownerId: Long,
        @Query("item_id") postId: Long
    ): LikesCountResponseDto

    @GET("wall.getComments?v=5.199&extended=1&fields=photo_100&count=20&thread_items_count=2&need_likes=1")
    suspend fun getComments(
        @Query("access_token") accessToken: String,
        @Query("owner_id") ownerId: Long,
        @Query("post_id") postId: Long,
    ): CommentsResponseDto

    @GET("wall.getComments?v=5.199&extended=1&fields=photo_100&count=20&thread_items_count=2&need_likes=1")
    suspend fun getComments(
        @Query("access_token") accessToken: String,
        @Query("owner_id") ownerId: Long,
        @Query("post_id") postId: Long,
        @Query("offset") offset: Int
    ): CommentsResponseDto


}