package com.example.vkapp.domain.useCase

import com.example.vkapp.domain.NewsFeedRepository
import javax.inject.Inject

class LoadNextFeedPostsUseCase @Inject constructor(private val newsFeedRepository: NewsFeedRepository) {

    suspend operator fun invoke() {
        return newsFeedRepository.loadNextFeedPosts()
    }

}