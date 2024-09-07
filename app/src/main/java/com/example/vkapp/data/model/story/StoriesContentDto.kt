package com.example.vkapp.data.model.story

import com.example.vkapp.data.model.GroupDto
import com.example.vkapp.data.model.ProfileDto
import com.google.gson.annotations.SerializedName

class StoriesContentDto(
    @SerializedName("items") val stories: List<StoryDto>?,
    @SerializedName("profiles") val profiles: List<ProfileDto>?,
    @SerializedName("groups") val groups: List<GroupDto>?,
)