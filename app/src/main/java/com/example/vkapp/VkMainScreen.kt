package com.example.vkapp

import android.annotation.SuppressLint
import androidx.compose.material.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import com.example.vkapp.domain.FeedPost

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Preview(showBackground = true)
@Composable
fun MainScreen() {

    val feedPost = remember { mutableStateOf(FeedPost()) }
    Scaffold(bottomBar = {
        NavigationBar(containerColor = MaterialTheme.colorScheme.primary) {
            val selectedItem = remember { mutableStateOf(0) }
            val items =
                listOf(NavigationItem.Home, NavigationItem.Favourite, NavigationItem.Profile)
            items.forEachIndexed { index, navigationItem ->
                NavigationBarItem(
                    selected = selectedItem.value == index,
                    onClick = { selectedItem.value = index },
                    icon = { Icon(navigationItem.icon, contentDescription = navigationItem.title) },
                    label = { Text(text = navigationItem.title) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.onPrimary,
                        unselectedIconColor = MaterialTheme.colorScheme.onSecondary
                    )
                )
            }
        }
    }) {
        PostCard(
            modifier = Modifier.padding(8.dp),
            feedPost = feedPost.value,
            onStatisticsClickListener = { newItem ->
                feedPost.value = feedPost.value.copy(
                    statistics = feedPost.value.statistics.map { oldItem ->
                        if (oldItem.statisticType == newItem.statisticType) {
                            oldItem.copy(count = oldItem.count + 1)
                        } else {
                            oldItem
                        }
                    }
                )
            }
        )
    }
}