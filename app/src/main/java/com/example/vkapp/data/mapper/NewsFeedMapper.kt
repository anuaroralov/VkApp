package com.example.vkapp.data.mapper

import com.example.vkapp.data.model.feedPost.CommentsResponseDto
import com.example.vkapp.data.model.feedPost.NewsFeedResponseDto
import com.example.vkapp.data.model.feedPost.PhotoUrlDto
import com.example.vkapp.domain.FeedPost
import com.example.vkapp.domain.Link
import com.example.vkapp.domain.PostComment
import com.example.vkapp.domain.StatisticItem
import com.example.vkapp.domain.StatisticType
import com.example.vkapp.domain.Video
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

        var isLiked = false
        var likesCount: Int? = null
        if (post.likes.canLike == 1) {
            isLiked = (post.likes.userLikes > 0)
            likesCount = post.likes.count
        }

        val commentsCount = if (post.comments?.canPost == 1) {
            post.comments.count
        } else {
            null
        }

        val contentText = post.text

        val contentImageUrls = post.attachments?.mapNotNull { attachment ->
            attachment.photo?.photoUrls?.lastOrNull()?.url
        }

        val contentVideos = post.attachments?.mapNotNull { attachment ->
            if (attachment.type == "video" && attachment.video != null) {
                Video(
                    id = attachment.video.id,
                    ownerId = attachment.video.ownerId,
                    title = attachment.video.title,
                    description = attachment.video.description,
                    duration = attachment.video.duration,
                    thumbnailUrl = getHighestQualityPhoto(attachment.video.image),
                    views = attachment.video.views,
                    comments = attachment.video.comments
                )
            } else null
        }

        val contentLinks = post.attachments?.mapNotNull { attachment ->
            if (attachment.type == "link" && attachment.link != null) {
                Link(
                    url = attachment.link.url,
                    caption = attachment.link.caption,
                    title = attachment.link.title,
                    photo = getHighestQualityPhoto(attachment.link.photo?.photoUrls)
                )
            } else null
        }

        val feedPost = FeedPost(
            id = post.id,
            communityId = post.communityId,
            communityName = group.name,
            publicationDate = mapTimestampToDate(post.date),
            communityImageUrl = group.imageUrl,
            contentText = contentText,
            contentImageUrls = contentImageUrls,
            contentVideos = contentVideos,
            contentLinks = contentLinks,
            statistics = listOf(
                StatisticItem(type = StatisticType.LIKES, likesCount),
                StatisticItem(type = StatisticType.VIEWS, post.views?.count ?: null),
                StatisticItem(type = StatisticType.SHARES, post.reposts?.count ?: null),
                StatisticItem(type = StatisticType.COMMENTS, commentsCount)
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

fun getHighestQualityPhoto(photos: List<PhotoUrlDto>?): String? {
    if (photos == null) return null
    return photos.maxByOrNull { it.width * it.height }?.url
}
