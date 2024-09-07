package com.example.vkapp.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import com.example.vkapp.R

sealed class NavigationItem(val screen: Screen, val title: Int, val icon: Any) {
    data object Home : NavigationItem(
        screen = Screen.Home,
        title = R.string.home,
        icon = Icons.Outlined.Home
    )

    data object Messages : NavigationItem(
        screen = Screen.Favourite,
        title = R.string.messages,
        icon = R.drawable.baseline_message_24
    )

    data object Profile : NavigationItem(
        screen = Screen.Profile,
        title = R.string.profile,
        icon = Icons.Outlined.Person
    )
}
