package com.example.vkapp.data.model.feedPost

import com.google.gson.annotations.SerializedName

data class AttachmentDto(
    @SerializedName("type") val type: String,
    @SerializedName("photo") val photo: PhotoDto?,
    @SerializedName("video") val video: VideoDto?,
    @SerializedName("link") val link: LinkDto?
)
