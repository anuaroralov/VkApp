package com.example.vkapp.domain.useCase

import com.example.vkapp.domain.NewsFeedRepository
import com.example.vkapp.domain.entity.StoriesResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetStoriesUseCase @Inject constructor(private val newsFeedRepository: NewsFeedRepository) {

    operator fun invoke(): Flow<StoriesResult> {
        return newsFeedRepository.getStories()
    }

}