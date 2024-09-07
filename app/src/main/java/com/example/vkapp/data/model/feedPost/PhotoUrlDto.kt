package com.example.vkapp.data.model.feedPost

import com.google.gson.annotations.SerializedName

data class PhotoUrlDto(
    @SerializedName("type") val type: String,
    @SerializedName("url") val url: String,
    @SerializedName("width") val width: Int,
    @SerializedName("height") val height: Int,
)
