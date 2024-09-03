package com.example.vkapp.domain.useCase

import com.example.vkapp.domain.NewsFeedRepository

class LoadNextFeedPosts(private val newsFeedRepository: NewsFeedRepository) {

    suspend operator fun invoke() {
        return newsFeedRepository.loadNextFeedPosts()
    }

}