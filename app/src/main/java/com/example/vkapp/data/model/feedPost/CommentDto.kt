package com.example.vkapp.data.model.feedPost

import com.google.gson.annotations.SerializedName

data class CommentDto(
    @SerializedName("id") val id: Long,
    @SerializedName("from_id") val authorId: Long,
    @SerializedName("text") val text: String,
    @SerializedName("date") val date: Long,
    @SerializedName("likes") val likes: LikesDto,
    @SerializedName("thread") val replies: CommentsThreadDto
)