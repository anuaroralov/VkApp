package com.example.vkapp.di

import androidx.lifecycle.ViewModel
import com.example.vkapp.presentation.home.newsFeed.NewsFeedViewModel
import com.example.vkapp.presentation.home.stories.StoriesViewModel
import com.example.vkapp.presentation.main.MainViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ViewModelModule {

    @IntoMap
    @ViewModelKey(NewsFeedViewModel::class)
    @Binds
    fun bindNewsFeedViewModel(viewModel: NewsFeedViewModel): ViewModel

    @IntoMap
    @ViewModelKey(MainViewModel::class)
    @Binds
    fun bindMainViewModel(viewModel: MainViewModel): ViewModel

    @IntoMap
    @ViewModelKey(StoriesViewModel::class)
    @Binds
    fun bindStoryViewModel(viewModel: StoriesViewModel): ViewModel
}