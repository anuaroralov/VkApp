package com.example.vkapp.presentation

import android.app.Application
import com.example.vkapp.di.ApplicationComponent
import com.example.vkapp.di.DaggerApplicationComponent
import com.vk.id.VKID

class VkApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        VKID.init(this)
    }

    val component: ApplicationComponent by lazy {
        DaggerApplicationComponent.factory().create(this)
    }
}