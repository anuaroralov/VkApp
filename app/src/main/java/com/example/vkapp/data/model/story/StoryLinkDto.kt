package com.example.vkapp.data.model.story

import com.google.gson.annotations.SerializedName

data class StoryLinkDto(
    @SerializedName("url") val url: String,
    @SerializedName("text") val text: String,
)