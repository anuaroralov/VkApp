package com.example.vkapp.data.model.story

import com.google.gson.annotations.SerializedName

data class StoriesResponseDto(
    @SerializedName("response") val content: StoriesContentDto

)