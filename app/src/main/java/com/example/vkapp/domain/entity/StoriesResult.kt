package com.example.vkapp.domain.entity


sealed class StoriesResult {
    data class Error(val exception: Throwable) : StoriesResult()
    data class Success(val stories: List<Story>) : StoriesResult()
    data object Loading : StoriesResult()


}