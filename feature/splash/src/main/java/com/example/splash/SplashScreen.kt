package com.example.splash

import android.provider.UserDictionary.Words.APP_ID
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.util.LottieLoader
import io.realm.kotlin.mongodb.App
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    navigateToHome:()->Unit,
    navigateToAuth:()->Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center,
    ) {
        var animateLogo by remember { mutableStateOf(true) }

        if (animateLogo) LottieLoader(
            modifier = Modifier.size(300.dp),
            lottieFile = com.example.ui.R.raw.splash_light_lottie
        )

        LaunchedEffect(Unit) {
            delay(5000)
            animateLogo = false
            val user = App.Companion.create(APP_ID).currentUser
            if (user != null && user.loggedIn) navigateToHome()
            else navigateToAuth()
        }
    }
}
