package com.example.diaryandroid.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import com.example.diaryandroid.data.MongoDB
import com.example.diaryandroid.presentation.ui.theme.DiaryAndroidTheme
import com.ramcosta.composedestinations.DestinationsNavHost


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)
        setContent {
            DiaryAndroidTheme {
                DestinationsNavHost(navGraph = NavGraphs.root)
            }
        }
    }
}