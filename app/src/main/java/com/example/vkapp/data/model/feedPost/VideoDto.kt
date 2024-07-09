package com.example.vkapp.data.model.feedPost

import com.google.gson.annotations.SerializedName

data class VideoDto(
    @SerializedName("id") val id: Int,
    @SerializedName("owner_id") val ownerId: Int,
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String,
    @SerializedName("duration") val duration: Int,
    @SerializedName("image") val image: List<PhotoUrlDto>,
    @SerializedName("views") val views: Int,
    @SerializedName("comments") val comments: Int
)