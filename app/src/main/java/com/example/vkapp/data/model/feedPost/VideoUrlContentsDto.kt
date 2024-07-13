package com.example.vkapp.data.model.feedPost

import com.google.gson.annotations.SerializedName

data class VideoUrlContentsDto(
    @SerializedName("items") val videoUrls: List<VideoUrlDto>)