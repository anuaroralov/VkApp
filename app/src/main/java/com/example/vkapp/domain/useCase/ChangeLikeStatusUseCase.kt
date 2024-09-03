package com.example.vkapp.domain.useCase

import com.example.vkapp.domain.NewsFeedRepository
import com.example.vkapp.domain.entity.FeedPost

class ChangeLikeStatusUseCase(private val newsFeedRepository: NewsFeedRepository) {

    suspend operator fun invoke(feedPost: FeedPost) {
        return newsFeedRepository.changeLikeStatus(feedPost)
    }

}