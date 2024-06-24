package com.example.vkapp.presentation.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.vk.api.sdk.VKPreferencesKeyValueStorage
import com.vk.api.sdk.auth.VKAccessToken
import com.vk.api.sdk.auth.VKAuthenticationResult

class MainViewModel(application: Application):AndroidViewModel(application) {
    private val _authState=MutableLiveData<AuthState>(AuthState.Initial)
    val authState: LiveData<AuthState> = _authState

    init {
        val storage=VKPreferencesKeyValueStorage(application)
        val token=VKAccessToken.restore(storage)
        Log.d("MainViewModel", "token=${token?.accessToken}")
        val loggedIn=token!=null && token.isValid
        _authState.value= if (loggedIn) AuthState.Authorized else AuthState.NotAuthorized
    }

    fun performAuth(result: VKAuthenticationResult) {
        if (result is VKAuthenticationResult.Success) {
            _authState.value = AuthState.Authorized
        } else {
            _authState.value = AuthState.NotAuthorized
        }
    }
}