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

    private val _user = MutableLiveData<VKIDUser>()
    val user: LiveData<VKIDUser> = _user

    init {
        checkAuthorization()
    }

    private fun checkAuthorization() {
        val token = VKID.instance.accessToken
        Log.d("MainViewModel", "token=${token?.token}")
        val loggedIn = token != null
        if (loggedIn) {
            if (accessTokenIsExpired()) {
                Log.d("MainViewModel", "token is expired")
                refreshAccessToken()
            }
            getUserInfo()
        } else {
            _authState.value = AuthState.NotAuthorized
        }
    }

    fun performAuth() {
        _authState.value = AuthState.Authorized
        Log.d("MainViewModel", "token=${VKID.instance.accessToken?.token}")
    }

    private fun getUserInfo() {
        viewModelScope.launch {
            VKID.instance.getUserData(callback = object : VKIDGetUserCallback {
                override fun onFail(fail: VKIDGetUserFail) {
                    when (fail) {
                        is VKIDGetUserFail.FailedApiCall -> Log.e(
                            "MainViewModel",
                            "API Call Failed: ${fail.description}"
                        )

                        is VKIDGetUserFail.IdTokenTokenExpired -> Log.e(
                            "MainViewModel",
                            "Refresh Token Expired "
                        )

                        is VKIDGetUserFail.NotAuthenticated -> Log.e(
                            "MainViewModel",
                            "User Unauthorized"
                        )
                    }
                }

                override fun onSuccess(user: VKIDUser) {
                    Log.d("MainViewModel", "user=$user")
                    _user.value = user
                    _authState.value = AuthState.Authorized
                }
            })

        }
    }

    private fun accessTokenIsExpired(): Boolean {
        val isExpired: Boolean =
            VKID.instance.accessToken?.expireTime!! < System.currentTimeMillis()
        return isExpired
    }

    private fun refreshAccessToken() {
        viewModelScope.launch {
            VKID.instance.refreshToken(callback = object : VKIDRefreshTokenCallback {
                override fun onSuccess(token: AccessToken) {
                    Log.d("MainViewModel", "token after refresh=${token.token}")
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
            })
        }
    }
}