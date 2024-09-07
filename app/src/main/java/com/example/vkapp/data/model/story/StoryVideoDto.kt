package com.example.vkapp.data.model.story

import com.google.gson.annotations.SerializedName

data class StoryVideoDto(
    @SerializedName("files") val file: StoryVideoFileDto,
    @SerializedName("duration") val duration: Int
)