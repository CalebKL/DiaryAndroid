package com.example.diaryandroid.presentation.auth

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.diaryandroid.presentation.auth.components.AuthenticationContent
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Destination
fun AuthenticationScreen(
    navigator: DestinationsNavigator?
) {
    Scaffold(
        content = {
            val unUsedPadding = it
            AuthenticationContent(
                onButtonClicked = {

                },
                loadingState = false
            )
        }
    )
}