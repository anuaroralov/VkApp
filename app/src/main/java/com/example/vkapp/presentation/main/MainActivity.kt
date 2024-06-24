package com.example.vkapp.presentation.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.vkapp.ui.theme.VkAppTheme
import com.vk.api.sdk.VK
import com.vk.api.sdk.auth.VKScope

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            VkAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val viewModel: MainViewModel = viewModel()
                    val authState = viewModel.authState.observeAsState()

                    val authLauncher =
                        rememberLauncherForActivityResult(contract = VK.getVKAuthActivityResultContract()) {
                            viewModel.performAuth(it)
                        }
                    when (authState.value) {
                        AuthState.Authorized -> MainScreen()

                        AuthState.NotAuthorized -> LaunchedEffect(key1 = true) {
                            authLauncher.launch(
                                arrayListOf(
                                    VKScope.NOTIFY,
                                    VKScope.FRIENDS,
                                    VKScope.PHOTOS,
                                    VKScope.AUDIO,
                                    VKScope.VIDEO,
                                    VKScope.STORIES,
                                    VKScope.PAGES,
                                    VKScope.STATUS,
                                    VKScope.NOTES,
                                    VKScope.MESSAGES,
                                    VKScope.WALL,
                                    VKScope.ADS,
                                    VKScope.OFFLINE,
                                    VKScope.DOCS,
                                    VKScope.GROUPS,
                                    VKScope.NOTIFICATIONS,
                                    VKScope.STATS,
                                    VKScope.EMAIL,
                                    VKScope.MARKET
                                )
                            )
                        }

                        else -> {}
                    }
                }
            }
        }
    }
}
