package com.example.vkapp.data.model.feedPost

import com.google.gson.annotations.SerializedName

data class CommentsDto(
    @SerializedName("count") val count: Int,
    @SerializedName("can_post") val canPost: Int?,
    @SerializedName("can_view") val canView: Int
)
