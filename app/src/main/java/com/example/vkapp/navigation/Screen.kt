package com.example.vkapp.navigation

import android.net.Uri
import com.example.vkapp.domain.entity.FeedPost
import com.google.gson.Gson

sealed class Screen(val route: String) {

    data object Home : Screen(ROUTE_HOME)

    data object NewsFeed : Screen(ROUTE_NEWS_FEED)

    data object Comments : Screen(ROUTE_COMMENTS) {

        fun getRouteWithArgs(feedPost: FeedPost): String {
            val feedPostJson = Gson().toJson(feedPost)
            return "comments/${feedPostJson.encode()}"
        }
    }

    data object Profile : Screen(ROUTE_PROFILE)

    data object Favourite : Screen(ROUTE_FAVOURITE)

    companion object {
        const val KEY_FEED_POST = "feed_post"

        const val ROUTE_HOME = "home"
        const val ROUTE_COMMENTS = "comments/{$KEY_FEED_POST}"
        const val ROUTE_NEWS_FEED = "news_feed"
        const val ROUTE_FAVOURITE = "favourite"
        const val ROUTE_PROFILE = "profile"
    }
}

fun String.encode(): String {
    return Uri.encode(this)
}