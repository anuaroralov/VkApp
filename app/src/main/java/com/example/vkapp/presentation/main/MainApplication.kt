package com.example.vkapp.presentation.main

import android.app.Application
import com.vk.id.VKID

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        VKID.init(this)
    }
}