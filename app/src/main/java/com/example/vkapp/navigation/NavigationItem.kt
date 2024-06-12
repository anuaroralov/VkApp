package com.example.vkapp.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.ui.graphics.vector.ImageVector

sealed class NavigationItem(val screen: Screen, val title: String, val icon: ImageVector) {
    data object Home : NavigationItem(
        screen = Screen.Home,
        title = "Home",
        icon = Icons.Outlined.Home)

    data object Favourite : NavigationItem(
        screen = Screen.Favourite,
        title = "Favourite",
        icon = Icons.Outlined.Favorite)

    data object Profile :
        NavigationItem(screen = Screen.Profile, title = "Profile", icon = Icons.Outlined.Person)
}