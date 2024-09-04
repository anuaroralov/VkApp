package com.example.vkapp.presentation.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.vkapp.presentation.ViewModelFactory
import com.example.vkapp.presentation.VkApplication
import com.example.vkapp.ui.theme.VkAppTheme
import javax.inject.Inject

class MainActivity : ComponentActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val component by lazy {
        (application as VkApplication).component
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        component.inject(this)
        super.onCreate(savedInstanceState)

        setContent {
            VkAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val viewModel: MainViewModel = viewModel()

                    val authState = viewModel.authState.observeAsState(AuthState.Initial)

                    MainScreenContent(authState, viewModel, viewModelFactory)
                }
            }
        }
    }

}

@Composable
fun MainScreenContent(
    authState: State<AuthState>,
    viewModel: MainViewModel,
    viewModelFactory: ViewModelFactory
) {
    when (authState.value) {
        AuthState.Authorized -> {
            MainScreen(viewModel.user.value, viewModelFactory)
        }

        AuthState.NotAuthorized -> {
            AuthScreen(viewModel)
        }

        else -> {}
    }
}



