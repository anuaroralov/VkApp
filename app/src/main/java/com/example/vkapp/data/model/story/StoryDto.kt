package com.example.vkapp.data.model.story

import com.google.gson.annotations.SerializedName

data class StoryDto(
    @SerializedName("id") val id: String,
    @SerializedName("has_unseen") val hasUnseen: Boolean,
    @SerializedName("stories") val stories: List<StoryItemDto>,
    @SerializedName("name") val ownerName: String,
)