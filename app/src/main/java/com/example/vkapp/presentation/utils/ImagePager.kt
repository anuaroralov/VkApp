package com.example.vkapp.presentation.utils

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ImagePager(
    imageUrls: List<String>
) {
    val pagerState = rememberPagerState(pageCount = { imageUrls.size })

    Box {
        HorizontalPager(state = pagerState) { page ->
            AsyncImage(
                model = imageUrls[page],
                modifier = Modifier.fillMaxWidth(),
                contentDescription = null,
                contentScale = ContentScale.Crop
            )
        }
        if (imageUrls.size > 1) {
            Text(
                text = "${pagerState.currentPage + 1}/${pagerState.pageCount}",
                color = Color.White,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
                    .background(Color.Black.copy(alpha = 0.5f), shape = RoundedCornerShape(16.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            )
        }
    }
}