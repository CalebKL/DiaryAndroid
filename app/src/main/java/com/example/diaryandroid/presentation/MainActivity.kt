package com.example.diaryandroid.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import com.example.diaryandroid.navigation.Screen
import com.example.diaryandroid.navigation.SetupNavGraph
import com.example.diaryandroid.presentation.ui.theme.DiaryAndroidTheme
import com.google.firebase.FirebaseApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        FirebaseApp.initializeApp(this)
        super.onCreate(savedInstanceState)
        setContent {
            DiaryAndroidTheme {
                val navController = rememberNavController()
                SetupNavGraph(startDestination = Screen.Splash.route, navController =navController)
            }
        }
    }
}