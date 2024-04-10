package com.example.vkapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.vkapp.domain.FeedPost
import com.example.vkapp.domain.StatisticItem

class MyViewModel : ViewModel() {

    private val sourceList = mutableListOf<FeedPost>().apply {
        repeat(10) {
            add(FeedPost(id = it))
        }
    }

    private val _feedPosts = MutableLiveData<List<FeedPost>>(sourceList)
    val feedPosts: LiveData<List<FeedPost>> get() = _feedPosts

    fun updateCount(feedPost: FeedPost, statisticItem: StatisticItem) {
        val updatedPosts = _feedPosts.value?.map { currentFeedPost ->
            if (currentFeedPost == feedPost) {
                val currentStatistics = currentFeedPost.statistics
                currentFeedPost.copy(
                    statistics = currentStatistics.map { oldItem ->
                        if (oldItem.statisticType == statisticItem.statisticType) {
                            oldItem.copy(count = oldItem.count + 1)
                        } else {
                            oldItem
                        }
                    }
                )
            } else {
                currentFeedPost
            }
        }
        _feedPosts.value = updatedPosts
    }

    fun removeFeedPost(feedPost: FeedPost) {
        val oldPost = feedPosts.value?.toMutableList() ?: mutableListOf()
        oldPost.remove(feedPost)
        _feedPosts.value = oldPost
    }

}