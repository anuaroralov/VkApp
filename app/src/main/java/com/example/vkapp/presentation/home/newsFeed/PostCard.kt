package com.example.vkapp.presentation.home.newsFeed

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.vkapp.domain.entity.FeedPost
import com.example.vkapp.domain.entity.StatisticItem
import com.example.vkapp.presentation.utils.ExpandableText
import com.example.vkapp.presentation.utils.ImagePager

@Composable
fun PostCard(
    modifier: Modifier = Modifier,
    feedPost: FeedPost,
    onLikeClickListener: (StatisticItem) -> Unit,
    onShareClickListener: (StatisticItem) -> Unit,
    onCommentClickListener: (StatisticItem) -> Unit,
    onLinkClickListener: (String) -> Unit
) {
    var textIsExpanded = remember { mutableStateOf(false) }

    Card(
        shape = RoundedCornerShape(12.dp),
        modifier = modifier,
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surface),
    ) {
        Column(Modifier.padding(8.dp)) {
            PostHeader(feedPost)
            if (feedPost.contentText != null) {
                ExpandableText(
                    text = feedPost.contentText,
                    isExpanded = textIsExpanded.value,
                    onLinkClickListener = onLinkClickListener,
                    onExpandClick = { textIsExpanded.value = !textIsExpanded.value },
                    tint = MaterialTheme.colorScheme.onBackground,
                    size = 16.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            if (feedPost.contentImageUrls != null) {
                ImagePager(imageUrls = feedPost.contentImageUrls)
                Spacer(modifier = Modifier.height(8.dp))
            }

            if (feedPost.contentVideos != null) {
                feedPost.contentVideos.forEach { video ->
                    VideoCard(video = video, onLinkClickListener = onLinkClickListener)
                }
                Spacer(modifier = Modifier.height(8.dp))
            }

            feedPost.contentLinks?.forEach { link ->
                LinkCard(link = link, onLinkClickListener = onLinkClickListener)
                Spacer(modifier = Modifier.height(8.dp))
            }

            Statistic(
                statistics = feedPost.statistics,
                onLikeClickListener = onLikeClickListener,
                onShareClickListener = onShareClickListener,
                onCommentClickListener = onCommentClickListener,
                isFavourite = feedPost.isLiked
            )
        }
    }
}

@Composable
fun PostHeader(feedPost: FeedPost) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
    ) {
        AsyncImage(
            model = feedPost.communityImageUrl,
            contentDescription = null,
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = feedPost.communityName,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = feedPost.publicationDate, fontSize = 14.sp)
        }
        Icon(
            imageVector = Icons.Rounded.MoreVert, contentDescription = null
        )
    }
}



