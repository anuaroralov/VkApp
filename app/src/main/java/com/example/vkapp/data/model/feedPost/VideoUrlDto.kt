package com.example.vkapp.data.model.feedPost

import com.google.gson.annotations.SerializedName

data class VideoUrlDto(
    @SerializedName("player") val videoUrl: String,
)