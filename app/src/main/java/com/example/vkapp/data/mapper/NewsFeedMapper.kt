package com.example.vkapp.data.mapper

import com.example.vkapp.data.model.CommentsResponseDto
import com.example.vkapp.data.model.NewsFeedResponseDto
import com.example.vkapp.domain.FeedPost
import com.example.vkapp.domain.PostComment
import com.example.vkapp.domain.StatisticItem
import com.example.vkapp.domain.StatisticType
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.absoluteValue

internal fun NewsFeedResponseDto.mapResponseToPosts(): List<FeedPost> {
    val result = mutableListOf<FeedPost>()

    val posts = newsFeedContent.posts
    val groups = newsFeedContent.groups

    for (post in posts) {
        val group = groups.find { it.id == post.communityId.absoluteValue }

        // Проверка, что group, post.id, и post.likes не null
        if (group == null || post.id == null || post.likes == null) {
            continue
        }

        val isLiked = (post.likes.userLikes > 0 )
        val contentText = post.text?:""
        val feedPost = FeedPost(
            id = post.id,
            communityId = post.communityId,
            communityName = group.name,
            publicationDate = mapTimestampToDate(post.date),
            communityImageUrl = group.imageUrl,
            contentText = contentText,
            contentImageUrls = post.attachments?.mapNotNull { attachment ->
            attachment.photo?.photoUrls?.lastOrNull()?.url },
            statistics = listOf(
                StatisticItem(type = StatisticType.LIKES, post.likes.count),
                StatisticItem(type = StatisticType.VIEWS, post.views.count),
                StatisticItem(type = StatisticType.SHARES, post.reposts.count),
                StatisticItem(type = StatisticType.COMMENTS, post.comments.count)
            ),
            isLiked = isLiked
        )
        result.add(feedPost)
    }
    return result
}

internal fun CommentsResponseDto.mapResponseToComments(): List<PostComment> {
    val result = mutableListOf<PostComment>()
    val comments = content.comments
    val profiles = content.profiles
    for (comment in comments) {
        if (comment.text.isBlank()) continue
        val author = profiles.firstOrNull { it.id == comment.authorId } ?: continue
        val postComment = PostComment(
            id = comment.id,
            authorName = "${author.firstName} ${author.lastName}",
            authorAvatarUrl = author.avatarUrl,
            commentText = comment.text,
            publicationDate = mapTimestampToDate(comment.date)
        )
        result.add(postComment)
    }
    return result
}


private fun mapTimestampToDate(timestamp: Long): String {
    val date = Date(timestamp * 1000)
    return SimpleDateFormat("d MMMM yyyy, hh:mm", Locale.getDefault()).format(date)
}
