package com.example.diaryandroid.presentation.write

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import com.example.diaryandroid.model.Diary
import com.example.diaryandroid.presentation.write.components.WriteTopBar
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Destination
fun WriteScreen(
    navigator: DestinationsNavigator?
) {
    Scaffold(
        topBar = {
                 WriteTopBar(
                     onBackPressed = {
                         navigator?.popBackStack()
                     },
                     selectedDiary =null,
                     onDeleteConfirmed = {}
                 )
        },
        content = {
            val unUsedPadding = it
        }
    )
}