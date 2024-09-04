package com.example.vkapp.domain.useCase

import com.example.vkapp.domain.NewsFeedRepository
import com.example.vkapp.domain.entity.NewsFeedResult
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class GetRecommendationsUseCase @Inject constructor(private val newsFeedRepository: NewsFeedRepository) {

    operator fun invoke(): StateFlow<NewsFeedResult>{
        return newsFeedRepository.getRecommendations()
    }

}