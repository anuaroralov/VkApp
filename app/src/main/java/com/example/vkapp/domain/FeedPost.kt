package com.example.vkapp.domain

import com.example.vkapp.R

data class FeedPost(
    val communityName: String = "/dev/null",
    val publishedDate: String = "14:00",
    val avatarResId: Int = R.drawable.ic_launcher_background,
    val contentText: String = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAa",
    val contentResId: Int = R.drawable.ic_launcher_background,
    val statistics: List<StatisticItem> = listOf(
        StatisticItem(StatisticType.VIEWS, 15),
        StatisticItem(StatisticType.SHARES, 54),
        StatisticItem(StatisticType.COMMENTS, 84),
        StatisticItem(StatisticType.LIKES, 825)
    )
)