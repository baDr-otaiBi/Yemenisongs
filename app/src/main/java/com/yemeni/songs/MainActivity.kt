package com.yemeni.songs

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.yemeni.songs.navigation.Screen
import com.yemeni.songs.player.MusicPlayer
import com.yemeni.songs.ui.components.MiniPlayer
import com.yemeni.songs.ui.screens.HomeScreen
import com.yemeni.songs.ui.screens.PlayerScreen
import com.yemeni.songs.ui.screens.SingerDetailScreen
import com.yemeni.songs.ui.screens.SplashScreen
import com.yemeni.songs.ui.theme.YemeniSongsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            YemeniSongsTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    YemeniSongsApp()
                }
            }
        }
    }
}

@Composable
fun YemeniSongsApp() {
    val context = LocalContext.current
    val navController = rememberNavController()
    val musicPlayer = remember { MusicPlayer(context) }
    var showFullPlayer by remember { mutableStateOf(false) }

    DisposableEffect(Unit) {
        onDispose { musicPlayer.release() }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Full screen player overlay
        if (showFullPlayer && musicPlayer.hasSong) {
            PlayerScreen(
                musicPlayer = musicPlayer,
                onDismiss = { showFullPlayer = false }
            )
        } else {
            // Main navigation
            NavHost(
                navController = navController,
                startDestination = Screen.Splash.route,
                enterTransition = {
                    fadeIn(tween(300)) + slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Start,
                        tween(300)
                    )
                },
                exitTransition = { fadeOut(tween(300)) },
                popEnterTransition = {
                    fadeIn(tween(300)) + slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.End,
                        tween(300)
                    )
                },
                popExitTransition = { fadeOut(tween(300)) },
            ) {
                composable(Screen.Splash.route) {
                    SplashScreen(
                        onEnterClick = {
                            navController.navigate(Screen.Home.route) {
                                popUpTo(Screen.Splash.route) { inclusive = true }
                            }
                        }
                    )
                }

                composable(Screen.Home.route) {
                    HomeScreen(
                        onSingerClick = { singerId ->
                            navController.navigate(Screen.SingerDetail.createRoute(singerId))
                        }
                    )
                }

                composable(
                    route = Screen.SingerDetail.route,
                    arguments = listOf(
                        navArgument("singerId") { type = NavType.IntType }
                    )
                ) { backStackEntry ->
                    val singerId = backStackEntry.arguments?.getInt("singerId")
                        ?: return@composable
                    SingerDetailScreen(
                        singerId = singerId,
                        musicPlayer = musicPlayer,
                        onBackClick = { navController.popBackStack() },
                        onSongClick = { song, singer, playlist, index ->
                            musicPlayer.playSong(song, singer, playlist, index)
                        },
                    )
                }
            }

            // Mini player at bottom
            if (musicPlayer.hasSong) {
                MiniPlayer(
                    musicPlayer = musicPlayer,
                    onClick = { showFullPlayer = true },
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .navigationBarsPadding(),
                )
            }
        }
    }
}
