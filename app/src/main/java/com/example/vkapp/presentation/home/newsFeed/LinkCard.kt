package com.example.vkapp.presentation.home.newsFeed

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.vkapp.domain.Link

@Composable
fun LinkCard(
    link: Link,
    onLinkClickListener: (String) -> Unit
) {
    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onLinkClickListener(link.url)
            },
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surfaceContainer)
    ) {
        Column {
            AsyncImage(
                model = link.photo,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .background(Color.Gray),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(8.dp))
            if (link.title != null) {
                Text(
                    text = link.title,
                    modifier = Modifier.padding(horizontal = 8.dp),
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(4.dp))
            }
            if (link.caption != null) {
                Text(
                    text = link.caption,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}