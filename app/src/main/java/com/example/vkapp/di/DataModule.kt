package com.example.vkapp.di

import com.example.vkapp.data.network.ApiFactory
import com.example.vkapp.data.network.ApiService
import com.example.vkapp.data.repository.NewsFeedRepositoryImpl
import com.example.vkapp.domain.NewsFeedRepository
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
interface DataModule {

    @ApplicationScope
    @Binds
    fun bindRepository(impl: NewsFeedRepositoryImpl): NewsFeedRepository

    companion object {

        @ApplicationScope
        @Provides
        fun provideApiService(): ApiService {
            return ApiFactory.apiService
        }

    }
}