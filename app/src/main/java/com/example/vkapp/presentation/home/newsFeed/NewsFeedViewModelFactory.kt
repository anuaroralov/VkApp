package com.example.vkapp.presentation.home.newsFeed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.vkapp.data.repository.NewsFeedRepository

class NewsFeedViewModelFactory() : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return NewsFeedViewModel( NewsFeedRepository()) as T
    }
}