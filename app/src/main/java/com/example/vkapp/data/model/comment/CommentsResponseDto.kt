package com.example.vkapp.data.model.comment

import com.google.gson.annotations.SerializedName

data class CommentsResponseDto(
    @SerializedName("response") val content: CommentsContentDto
)