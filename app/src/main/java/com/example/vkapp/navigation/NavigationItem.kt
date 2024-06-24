package com.example.vkapp.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.vkapp.R

sealed class NavigationItem(val screen: Screen, val title: Int, val icon: ImageVector) {
    data object Home : NavigationItem(
        screen = Screen.Home,
        title = R.string.home,
        icon = Icons.Outlined.Home)

    data object Favourite : NavigationItem(
        screen = Screen.Favourite,
        title = R.string.favourite,
        icon = Icons.Outlined.Favorite)

    data object Profile :
        NavigationItem(screen = Screen.Profile, title = R.string.profile, icon = Icons.Outlined.Person)
}