package com.example.vkapp.data.model.comment

import com.google.gson.annotations.SerializedName

data class CommentsThreadDto(
    @SerializedName("items") val items: List<CommentDto>,
    @SerializedName("count") val count: Int,
    @SerializedName("show_reply_button") val showReplyButton: Boolean,

    )