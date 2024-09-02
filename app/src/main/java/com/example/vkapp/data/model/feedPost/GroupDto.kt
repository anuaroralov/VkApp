package com.example.vkapp.data.model.feedPost

import com.google.gson.annotations.SerializedName

data class GroupDto(
    @SerializedName("id") val id: Long,
    @SerializedName("name") val name: String,
    @SerializedName("photo_100") val imageUrl: String
)
