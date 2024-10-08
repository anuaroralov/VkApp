package com.example.vkapp.domain.useCase

import com.example.vkapp.domain.NewsFeedRepository
import com.example.vkapp.domain.entity.CommentsResult
import com.example.vkapp.domain.entity.FeedPost
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCommentsUseCase @Inject constructor(private val newsFeedRepository: NewsFeedRepository) {

    operator fun invoke(
        feedPost: FeedPost,
        offset: Int = 0,
        commentId: Long? = null
    ): Flow<CommentsResult> {
        return newsFeedRepository.getComments(feedPost, offset, commentId)
    }

}