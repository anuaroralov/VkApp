package com.example.vkapp.domain.useCase

import com.example.vkapp.domain.NewsFeedRepository
import com.example.vkapp.domain.entity.FeedPost
import javax.inject.Inject

class ChangeLikeStatusUseCase @Inject constructor(private val newsFeedRepository: NewsFeedRepository) {

    suspend operator fun invoke(feedPost: FeedPost) {
        return newsFeedRepository.changeLikeStatus(feedPost)
    }

}