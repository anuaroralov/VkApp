package com.example.vkapp.navigation

sealed class Screen(val route: String) {

    data object Home : Screen("HOME")
    data object Profile : Screen("PROFILE")

    data object Favourite : Screen("FAVOURITE")

}