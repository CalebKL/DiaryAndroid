package com.example.diaryandroid.splash

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.diaryandroid.util.LottieLoader
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.delay
import com.example.diaryandroid.R

@Destination(start = true)
@Composable
fun SplashScreen(
    navigator: DestinationsNavigator?
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        var animateLogo by remember { mutableStateOf(true) }

        if (animateLogo) LottieLoader(
            modifier = Modifier.size(270.dp),
            lottieFile = R.raw.splash_lottie
        )

        LaunchedEffect(Unit) {
            delay(2900)
            animateLogo = false
            navigator?.popBackStack()

        }
    }
}
