package com.example.vkapp.presentation.home.comments

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.vkapp.domain.PostComment

@Composable
fun CommentItem(
    comment: PostComment,
    onReply: (PostComment) -> Unit,
    isReply: Boolean = false // Flag to indicate if the comment is a reply
) {
    val paddingStart = if (isReply) 32.dp else 0.dp
    val textSize = if (isReply) 14.sp else 16.sp
    val imageSize = if (isReply) 36.dp else 40.dp
    val spacerSize = if (isReply) 0.dp else 2.dp

    Column(modifier = Modifier.padding(start = paddingStart)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp)
        ) {
            AsyncImage(
                modifier = Modifier
                    .size(imageSize)
                    .clip(CircleShape),
                model = comment.authorAvatarUrl,
                contentDescription = null
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = comment.authorName,
                    fontWeight = FontWeight.Bold,
                    fontSize = textSize,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(spacerSize))
                Text(
                    text = comment.commentText,
                    fontSize = textSize,
                    color = MaterialTheme.colorScheme.onBackground,
                )
                Spacer(modifier = Modifier.height(spacerSize))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = comment.publicationDate,
                        fontSize = textSize,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    ClickableText(
                        text = AnnotatedString("Reply"),
                        style = TextStyle(fontSize = textSize, color = Color.Gray),
                        onClick = {
                            onReply(comment)
                        },
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                }
            }
        }
        comment.replies.forEach { reply ->
            CommentItem(comment = reply, onReply = onReply, isReply = true)
        }
    }
}