package com.example.vkapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.vkapp.domain.FeedPost
import com.example.vkapp.domain.StatisticItem
import com.example.vkapp.domain.StatisticType

@Composable
fun PostCard(
    modifier: Modifier = Modifier,
    feedPost: FeedPost,
    onLikeClickListener: (StatisticItem) -> Unit,
    onShareClickListener: (StatisticItem) -> Unit,
    onCommentClickListener: (StatisticItem) -> Unit
) {
    Card(shape = RoundedCornerShape(4.dp), modifier = modifier) {
        Column(Modifier.padding(8.dp)) {
            PostHeader(feedPost)
            Text(text = feedPost.contentText)
            Spacer(modifier = Modifier.height(8.dp))
            Image(
                modifier = Modifier.fillMaxWidth(),
                painter = painterResource(id = feedPost.contentResId),
                contentDescription = null,
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(8.dp))
            Statistic(
                statistics = feedPost.statistics,
                onLikeClickListener=onLikeClickListener,
                onShareClickListener=onShareClickListener,
                onCommentClickListener=onCommentClickListener
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
        Image(
            painter = painterResource(id = feedPost.avatarResId),
            contentDescription = null,
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(text = feedPost.communityName)
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = feedPost.publishedDate)
        }
        Icon(
            imageVector = Icons.Rounded.MoreVert, contentDescription = null
        )
    }
}

@Composable
fun Statistic(
    statistics: List<StatisticItem>,
    onLikeClickListener: (StatisticItem) -> Unit,
    onShareClickListener: (StatisticItem) -> Unit,
    onCommentClickListener: (StatisticItem) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
    ) {
        val likesItem = statistics.getItemByType(StatisticType.LIKES)
        ActionButton(
            icon = R.drawable.baseline_favorite_border_24,
            count = likesItem.count.toString(),
            onItemClickListener = { onLikeClickListener(likesItem) }
        )
        Spacer(modifier = Modifier.width(2.dp))
        val commentsItem = statistics.getItemByType(StatisticType.COMMENTS)
        ActionButton(
            icon = R.drawable.baseline_comment_24,
            count = commentsItem.count.toString(),
            onItemClickListener = { onCommentClickListener(commentsItem) }
        )
        Spacer(modifier = Modifier.width(2.dp))
        val sharesItem = statistics.getItemByType(StatisticType.SHARES)
        ActionButton(
            icon = R.drawable.baseline_send_24,
            count = sharesItem.count.toString(),
            onItemClickListener = { onShareClickListener(sharesItem) }
        )
        Spacer(Modifier.weight(1f))
        Row {
            val viewsItem = statistics.getItemByType(StatisticType.VIEWS)
            Icon(
                painter = painterResource(id = R.drawable.baseline_visibility_24),
                contentDescription = "Views"
            )
            Spacer(modifier = Modifier.width(2.dp))
            Text(text = viewsItem.count.toString())
        }
    }
}

private fun List<StatisticItem>.getItemByType(type: StatisticType): StatisticItem {
    return this.find { it.statisticType == type }
        ?: throw IllegalStateException("not such type of statisticType")
}

@Composable
fun ActionButton(icon: Int, count: String, onItemClickListener: () -> Unit) {
    Surface(
        shape = RoundedCornerShape(50),
        modifier = Modifier
            .padding(4.dp)
            .clickable { onItemClickListener() },
        color = MaterialTheme.colorScheme.secondary
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(text = count)
        }
    }
}
