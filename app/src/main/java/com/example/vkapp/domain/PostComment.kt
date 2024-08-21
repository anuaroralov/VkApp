package com.example.vkapp.domain

data class PostComment(
    val id: Long,
    val authorName: String,
    val authorAvatarUrl: String,
    val commentText: String,
    val publicationDate: String,
    val replies: List<PostComment> = emptyList()
)