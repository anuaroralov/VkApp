package com.example.vkapp.presentation.home.newsFeed

import android.webkit.WebView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class VideoViewModel : ViewModel() {
    private val _isFullScreen = MutableLiveData(false)
    val isFullScreen: LiveData<Boolean> = _isFullScreen

    var webView: WebView? = null

    fun enterFullScreen() {
        _isFullScreen.value = true
    }

    fun exitFullScreen() {
        _isFullScreen.value = false
    }
}


