package com.example.vkapp.domain.entity

data class StoryItem(
    val id: Long,
    val photoUrl: String?,
    val videoUrl: String?,
    val link: Link?,
    val date: String,
)