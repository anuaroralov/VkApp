package com.example.vkapp.data.model.story

import com.google.gson.annotations.SerializedName

data class StoryVideoFileDto(
    @SerializedName("dash_webm") val dashWebm: String,
)