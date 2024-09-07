package com.example.vkapp.domain.entity

sealed class CommentsResult {
    data class Success(val comments: List<PostComment>) : CommentsResult()
    data class Error(val exception: Throwable) : CommentsResult()
    data object Loading : CommentsResult()
}
