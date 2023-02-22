package com.example.splash.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.splash.SplashScreen
import com.example.util.Screen

fun NavGraphBuilder.splashRoute(
    navigateToHome: () -> Unit,
    navigateToAuth:()->Unit
) {
    composable(route = Screen.Splash.route) {
        SplashScreen(
            navigateToHome = navigateToHome,
            navigateToAuth = navigateToAuth
        )
    }
}