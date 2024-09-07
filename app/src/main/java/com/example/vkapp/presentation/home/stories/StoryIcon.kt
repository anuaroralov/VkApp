package com.example.vkapp.presentation.home.stories

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.vkapp.domain.entity.Story
import com.vk.id.VKIDUser

@Composable
fun StoryIcon(itemSize: Dp = 80.dp, story: Story) {
    Column(
        modifier = Modifier
            .padding(4.dp, 12.dp, 4.dp, 4.dp)
            .width(itemSize)
            .background(MaterialTheme.colorScheme.surface),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = story.authorImg,
            contentDescription = null,
            modifier = Modifier
                .size(itemSize - 8.dp)
                .clip(CircleShape)
                .then(
                    if (story.hasSeenAll) {
                        Modifier
                    } else {
                        Modifier
                            .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape)
                            .border(3.dp, MaterialTheme.colorScheme.onPrimary, CircleShape)
                    }
                )
        )
        Text(
            text = story.authorName,
            color = if (story.hasSeenAll) Color.Gray else MaterialTheme.colorScheme.primary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun AddStory(itemSize: Dp = 80.dp, user: VKIDUser, addStory: () -> Unit) {
    Column(
        modifier = Modifier
            .padding(4.dp, 12.dp, 4.dp, 4.dp)
            .width(itemSize)
            .background(MaterialTheme.colorScheme.surface)
            .clickable { addStory },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            contentAlignment = Alignment.BottomEnd,
            modifier = Modifier.size(itemSize - 8.dp)
        ) {
            AsyncImage(
                model = user.photo200,
                contentDescription = null,
                modifier = Modifier
                    .size(itemSize - 8.dp)
                    .clip(CircleShape)
            )
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .background(MaterialTheme.colorScheme.primary, CircleShape)
                    .border(2.dp, MaterialTheme.colorScheme.surface, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add",
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
        Text(user.firstName, color = MaterialTheme.colorScheme.primary)
    }
}
