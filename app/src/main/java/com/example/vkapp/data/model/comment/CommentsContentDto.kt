package com.example.vkapp.data.model.comment

import com.example.vkapp.data.model.GroupDto
import com.example.vkapp.data.model.ProfileDto
import com.google.gson.annotations.SerializedName

data class CommentsContentDto(
    @SerializedName("items") val comments: List<CommentDto>,
    @SerializedName("profiles") val profiles: List<ProfileDto>,
    @SerializedName("groups") val groups: List<GroupDto>,

    )