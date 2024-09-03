package com.example.vkapp.data.mapper

import android.util.Log
import com.example.vkapp.data.model.feedPost.CommentDto
import com.example.vkapp.data.model.feedPost.CommentsResponseDto
import com.example.vkapp.data.model.feedPost.NewsFeedResponseDto
import com.example.vkapp.data.model.feedPost.PhotoUrlDto
import com.example.vkapp.domain.entity.CommentsReplies
import com.example.vkapp.domain.entity.FeedPost
import com.example.vkapp.domain.entity.Link
import com.example.vkapp.domain.entity.PostComment
import com.example.vkapp.domain.entity.StatisticItem
import com.example.vkapp.domain.entity.StatisticType
import com.example.vkapp.domain.entity.Video
import kotlinx.coroutines.runBlocking
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.absoluteValue

internal fun NewsFeedResponseDto.mapResponseToPosts(getVideo: suspend (String, String) -> String): List<FeedPost> {
    val result = mutableListOf<FeedPost>()

    val posts = newsFeedContent.posts
    val groups = newsFeedContent.groups

    for (post in posts) {
        val group = groups.find { it.id == post.communityId.absoluteValue } ?: continue

        val isLiked = (post.likes?.userLikes ?: 0) > 0
        val commentsCount = if (post.comments?.canView == 1) post.comments.count else null
        val contentText = post.text

        val contentImageUrls =
            post.attachments?.mapNotNull { it.photo?.photoUrls?.lastOrNull()?.url }

        val contentVideos = post.attachments?.mapNotNull { attachment ->
            if (attachment.type == "video" && attachment.video != null) {
                val videoUrl = runBlocking {
                    getVideo(attachment.video.ownerId.toString(), attachment.video.id.toString())
                }
                Video(
                    title = attachment.video.title,
                    description = attachment.video.description,
                    thumbnailUrl = getHighestQualityPhoto(attachment.video.image),
                    views = attachment.video.views,
                    comments = attachment.video.comments,
                    videoUrl = videoUrl
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
                StatisticItem(type = StatisticType.LIKES, post.likes?.count),
                StatisticItem(type = StatisticType.VIEWS, post.views?.count),
                StatisticItem(type = StatisticType.SHARES, post.reposts?.count),
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
    val groups = content.groups

    fun mapComment(comment: CommentDto): PostComment? {
        if (comment.text.isBlank()) return null

        val authorProfile = profiles.firstOrNull { it.id == comment.authorId }
        val authorGroup = groups.firstOrNull { it.id == comment.authorId.absoluteValue }

        val authorName: String
        val authorAvatarUrl: String

        Log.d("NewsFeedRepository", "authorProfile: $authorProfile, authorGroup: $authorGroup")
        when {
            authorProfile != null -> {
                authorName = "${authorProfile.firstName} ${authorProfile.lastName}"
                authorAvatarUrl = authorProfile.avatarUrl
            }

            authorGroup != null -> {
                authorName = authorGroup.name
                authorAvatarUrl = authorGroup.imageUrl
            }

            else -> {
                return null
            }
        }

        val replies = if (comment.replies == null) {
            null
        } else {
            CommentsReplies(
                items = comment.replies.items.mapNotNull { mapComment(it) },
                count = comment.replies.count,
            )
        }

        return PostComment(
            id = comment.id,
            authorName = authorName,
            authorAvatarUrl = authorAvatarUrl,
            commentText = comment.text,
            publicationDate = mapTimestampToDate(comment.date),
            replies = replies
        )
    }

    for (comment in comments) {
        mapComment(comment)?.let { result.add(it) }
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

