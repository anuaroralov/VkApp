package com.example.vkapp.presentation.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vk.id.VKIDAuthFail
import com.vk.id.auth.VKIDAuthUiParams
import com.vk.id.onetap.compose.onetap.OneTap

@Composable
fun AuthScreen(viewModel: MainViewModel) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center)
            .padding(16.dp)
    ) {
        OneTap(
            onAuth = { oAuth, accessToken -> viewModel.performAuth() },
            onFail = { oAuth, fail ->
                when (fail) {
                    is VKIDAuthFail.Canceled -> {
                        TODO()
                    }
                    is VKIDAuthFail.FailedApiCall -> {
                        TODO()
                    }
                    is VKIDAuthFail.FailedOAuth -> {
                        TODO()
                    }
                    is VKIDAuthFail.FailedOAuthState -> {
                        TODO()
                    }
                    is VKIDAuthFail.FailedRedirectActivity -> {
                        TODO()
                    }
                    is VKIDAuthFail.NoBrowserAvailable -> {
                        TODO()
                    }
                }
            },
            signInAnotherAccountButtonEnabled = true,
            authParams = VKIDAuthUiParams {
                scopes = setOf(
                    "phone",
                    "email",
                    "vkid.personal_info",
                    "friends",
                    "wall",
                    "groups",
                    "stories",
                    "docs",
                    "photos",
                    "ads",
                    "video",
                    "status",
                    "market",
                    "pages",
                    "notifications",
                    "stats",
                    "notes"
                )
            }
        )
    }
}