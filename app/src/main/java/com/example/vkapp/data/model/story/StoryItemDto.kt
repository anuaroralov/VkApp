package com.example.vkapp.data.model.story

import com.example.vkapp.data.model.feedPost.PhotoDto
import com.google.gson.annotations.SerializedName

data class StoryItemDto(
    @SerializedName("id") val id: Long,
    @SerializedName("owner_id") val ownerId: Long,
    @SerializedName("photo") val photo: PhotoDto?,
    @SerializedName("video") val video: StoryVideoDto?,
    @SerializedName("date") val date: Long,
    @SerializedName("link") val link: StoryLinkDto?
)