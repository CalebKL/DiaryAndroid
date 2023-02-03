package com.example.diaryandroid.presentation.splash

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
import com.example.diaryandroid.presentation.destinations.AuthenticationScreenDestination
import com.example.diaryandroid.presentation.destinations.HomeScreenDestination
import com.example.diaryandroid.util.Constants.APP_ID
import com.ramcosta.composedestinations.annotation.RootNavGraph
import io.realm.kotlin.mongodb.App

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
            modifier = Modifier.size(300.dp),
            lottieFile = R.raw.splash_light_lottie
        )

        LaunchedEffect(Unit) {
            delay(5000)
            animateLogo = false
            navigator?.popBackStack()
            val user = App.Companion.create(APP_ID).currentUser
            if (user != null && user.loggedIn) navigator?.navigate(HomeScreenDestination)
            else navigator?.navigate(AuthenticationScreenDestination)
        }
    }
}
