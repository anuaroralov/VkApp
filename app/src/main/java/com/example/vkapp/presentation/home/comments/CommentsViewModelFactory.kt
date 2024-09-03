package com.example.vkapp.presentation.home.comments

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.vkapp.domain.entity.FeedPost

class CommentsViewModelFactory(
    private val feedPost: FeedPost
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CommentsViewModel(feedPost) as T
    }
}