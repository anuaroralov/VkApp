package com.example.vkapp.presentation.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.vk.id.AccessToken
import com.vk.id.VKID
import com.vk.id.VKIDUser
import com.vk.id.refresh.VKIDRefreshTokenCallback
import com.vk.id.refresh.VKIDRefreshTokenFail
import com.vk.id.refreshuser.VKIDGetUserCallback
import com.vk.id.refreshuser.VKIDGetUserFail
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val _authState = MutableLiveData<AuthState>(AuthState.Initial)
    val authState: LiveData<AuthState> = _authState

    private val _user=MutableLiveData<VKIDUser>()
    val user:LiveData<VKIDUser> = _user

    init {
        checkAuthorization()
        getUserInfo()
    }

    private fun checkAuthorization() {
        val token = VKID.instance.accessToken
        Log.d("MainViewModel", "token=${token?.token}")
        val loggedIn = token != null
        if (loggedIn) {
//            Log.d("MainViewModel",VKID.instance.accessToken?.expireTime.toString()+"     "+System.currentTimeMillis())
            if (accessTokenIsExpired()) {
                Log.d("MainViewModel", "123")
                refreshAccessToken()
            } else {
                _authState.value = AuthState.Authorized
            }
        } else {
            _authState.value = AuthState.NotAuthorized
        }
    }

    fun performAuth() {
        _authState.value = AuthState.Authorized
        Log.d("MainViewModel", "token=${VKID.instance.accessToken?.token}")
    }

    private fun getUserInfo() {
        viewModelScope.launch() {
            try {
                VKID.instance.getUserData(
                    callback = object : VKIDGetUserCallback {
                        override fun onFail(fail: VKIDGetUserFail) {
                            when (fail) {
                                is VKIDGetUserFail.FailedApiCall -> TODO()
                                is VKIDGetUserFail.IdTokenTokenExpired -> TODO()
                                is VKIDGetUserFail.NotAuthenticated -> TODO()
                            }
                        }

                        override fun onSuccess(user: VKIDUser) {
                            Log.d("MainViewModel", "user=$user")
                            _user.value = user
                        }
                    }
                )
            } catch (e: Exception) {

            }
        }
    }


    private fun accessTokenIsExpired(): Boolean {
        val isExpired: Boolean
        if (VKID.instance.accessToken?.expireTime!! < System.currentTimeMillis()) {
            isExpired = true
        } else {
            isExpired = false
        }
        return isExpired
    }

    private fun refreshAccessToken() {
        viewModelScope.launch {
            VKID.instance.refreshToken(
                callback = object : VKIDRefreshTokenCallback {
                    override fun onSuccess(token: AccessToken) {
                        Log.d("MainViewModel", "token after refresh=${token}")
                        _authState.value = AuthState.Authorized
                    }

                    override fun onFail(fail: VKIDRefreshTokenFail) {
                        when (fail) {
                            is VKIDRefreshTokenFail.FailedApiCall -> {
                                Log.e("MainViewModel", "API Call Failed: ${fail.description}")
                            }

                            is VKIDRefreshTokenFail.RefreshTokenExpired -> {
                                _authState.value = AuthState.NotAuthorized
                                Log.e("MainViewModel", "Refresh Token Expired ")
                            }

                            is VKIDRefreshTokenFail.FailedOAuthState -> {
                                Log.e("MainViewModel", "OAuthState Failed: ${fail.description}")
                            }

                            is VKIDRefreshTokenFail.NotAuthenticated -> {
                                _authState.value = AuthState.NotAuthorized
                                Log.e("MainViewModel", "User Unauthorized")
                            }
                        }
                    }
                }
            )
        }
    }

}