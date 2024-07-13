package com.example.vkapp.data.model.feedPost

import com.google.gson.annotations.SerializedName

data class VideoUrlResponseDto(
    @SerializedName("response") val response: VideoUrlContentsDto
)