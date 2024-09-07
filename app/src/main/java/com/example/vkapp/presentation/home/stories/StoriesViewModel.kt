package com.example.vkapp.presentation.home.stories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vkapp.domain.entity.StoriesResult
import com.example.vkapp.domain.useCase.GetStoriesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class StoriesViewModel @Inject constructor(
    private val getStoriesUseCase: GetStoriesUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<StoriesScreenState>(StoriesScreenState.Initial)
    val state: StateFlow<StoriesScreenState> = _state.asStateFlow()

    init {
        loadStories()
    }

    private fun loadStories() {

        viewModelScope.launch {
            _state.value = StoriesScreenState.Loading
            getStoriesUseCase().collect {
                when (it) {
                    is StoriesResult.Error -> _state.value = StoriesScreenState.Error
                    is StoriesResult.Success -> _state.value =
                        StoriesScreenState.Stories(it.stories)

                    StoriesResult.Loading -> _state.value = StoriesScreenState.Loading
                }
            }
        }
    }
}