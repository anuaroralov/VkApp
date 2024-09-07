package com.example.vkapp.presentation.home.stories

import com.example.vkapp.domain.entity.Story

sealed class StoriesScreenState {

    data object Initial : StoriesScreenState()

    data class Stories(val stories: List<Story>) : StoriesScreenState()

    data object Loading : StoriesScreenState()

    data object Error : StoriesScreenState()

}
