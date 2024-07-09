package com.example.vkapp.data.model.feedPost

import com.google.gson.annotations.SerializedName

data class LikesDto(
    @SerializedName("count") val count: Int,
    @SerializedName("user_likes") val userLikes: Int,
    @SerializedName("can_like") val canLike: Int
)
