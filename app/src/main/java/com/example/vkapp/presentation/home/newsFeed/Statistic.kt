package com.example.vkapp.presentation.home.newsFeed

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.vkapp.R
import com.example.vkapp.domain.entity.StatisticItem
import com.example.vkapp.domain.entity.StatisticType

@Composable
fun Statistic(
    statistics: List<StatisticItem>,
    onLikeClickListener: (StatisticItem) -> Unit,
    onShareClickListener: (StatisticItem) -> Unit,
    onCommentClickListener: (StatisticItem) -> Unit,
    isFavourite: Boolean
) {
    Row(
        modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
    ) {
        val likesItem = statistics.getItemByType(StatisticType.LIKES)
        if (likesItem.count != null) {
            ActionButton(
                icon = if (isFavourite) R.drawable.baseline_favorite_24 else R.drawable.baseline_favorite_border_24,
                count = formatStatisticCount(likesItem.count),
                onItemClickListener = { onLikeClickListener(likesItem) },
                tint = if (isFavourite) Color.Red else Color.Gray,
                backgroundColor = if (isFavourite) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.background
            )
            Spacer(modifier = Modifier.width(2.dp))
        }

        val commentsItem = statistics.getItemByType(StatisticType.COMMENTS)
        if (commentsItem.count != null) {
            ActionButton(icon = R.drawable.baseline_comment_24,
                count = formatStatisticCount(commentsItem.count),
                onItemClickListener = { onCommentClickListener(commentsItem) })
            Spacer(modifier = Modifier.width(2.dp))
        }

        val sharesItem = statistics.getItemByType(StatisticType.SHARES)
        if (sharesItem.count != null) {
            ActionButton(icon = R.drawable.baseline_send_24,
                count = formatStatisticCount(sharesItem.count),
                onItemClickListener = { onShareClickListener(sharesItem) })
            Spacer(Modifier.weight(1f))
        }

        Row {
            val viewsItem = statistics.getItemByType(StatisticType.VIEWS)
            if (viewsItem.count != null) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_visibility_24),
                    contentDescription = "Views",
                    tint = Color.Gray
                )
                Spacer(modifier = Modifier.width(2.dp))
                Text(text = formatStatisticCount(viewsItem.count), color = Color.Gray)
            }
        }
    }
}

private fun formatStatisticCount(count: Int): String {
    return if (count > 100_000) {
        String.format("%sK", (count / 1000))
    } else if (count > 1000) {
        String.format("%.1fK", (count / 1000f))
    } else {
        count.toString()
    }
}

private fun List<StatisticItem>.getItemByType(type: StatisticType): StatisticItem {
    return this.find { it.type == type }
        ?: throw IllegalStateException("not such type of statisticType")
}

@Composable
fun ActionButton(
    icon: Int,
    count: String,
    onItemClickListener: () -> Unit,
    tint: Color = Color.Gray,
    backgroundColor: Color = MaterialTheme.colorScheme.background
) {
    Surface(
        shape = RoundedCornerShape(50),
        modifier = Modifier
            .padding(4.dp)
            .clickable { onItemClickListener() },
        color = backgroundColor
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = tint
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(text = count, color = tint)
        }
    }
}