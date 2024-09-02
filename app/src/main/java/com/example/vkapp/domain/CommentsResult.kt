package com.example.vkapp.domain

sealed class CommentsResult{
    data class Error(val exception: Throwable) : CommentsResult()
    data class Success(val comments: List<PostComment>) : CommentsResult()
    data object Loading : CommentsResult()
}