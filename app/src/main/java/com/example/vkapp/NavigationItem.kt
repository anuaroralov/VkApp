package com.example.vkapp

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.ui.graphics.vector.ImageVector

sealed class NavigationItem(val title:String,val icon:ImageVector) {
    data object Home:NavigationItem(title = "Home", icon = Icons.Outlined.Home)

    data object Favourite:NavigationItem(title = "Favourite", icon = Icons.Outlined.Favorite)

    data object Profile:NavigationItem(title = "Profile",icon=Icons.Outlined.Person)
}