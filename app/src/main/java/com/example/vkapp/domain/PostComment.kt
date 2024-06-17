package com.example.vkapp.domain

import com.example.vkapp.R

data class PostComment(
    val id: Int,
    val authorName: String = "Author",
    val authorAvatarId: Int = R.drawable.ic_launcher_background,
    val commentText: String = "Long comment text",
    val publicationDate: String = "14:00"
)