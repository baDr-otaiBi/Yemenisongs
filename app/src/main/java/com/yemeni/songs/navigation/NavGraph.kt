package com.yemeni.songs.navigation

sealed class Screen(val route: String) {
    data object Splash : Screen("splash")
    data object Home : Screen("home")
    data object SingerDetail : Screen("singer/{singerId}") {
        fun createRoute(singerId: Int) = "singer/$singerId"
    }
}
