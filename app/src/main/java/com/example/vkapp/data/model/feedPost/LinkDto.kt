package com.example.vkapp.data.model.feedPost

import com.google.gson.annotations.SerializedName

data class LinkDto(
    @SerializedName("url") val url: String,
    @SerializedName("caption") val caption: String?,
    @SerializedName("photo") val photo: PhotoDto?,
    @SerializedName("title") val title: String?

)