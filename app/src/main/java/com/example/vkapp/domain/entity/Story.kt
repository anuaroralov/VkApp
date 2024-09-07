package com.example.vkapp.domain.entity

data class Story(
    val id: String,
    val authorImg: String,
    val authorName: String,
    val stories: List<StoryItem>,
    val hasSeenAll: Boolean
)