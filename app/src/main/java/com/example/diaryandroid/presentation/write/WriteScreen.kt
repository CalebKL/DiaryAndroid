package com.example.diaryandroid.presentation.write

import Mood
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import com.example.diaryandroid.presentation.write.components.WriteContent
import com.example.diaryandroid.presentation.write.components.WriteTopBar
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.rememberPagerState
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.*
import com.example.diaryandroid.model.Diary
import com.google.accompanist.pager.PagerState

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPagerApi::class)
@Composable
fun WriteScreen(
    uiState: UiState,
    pagerState: PagerState,
    moodName: () -> String,
    onTitleChanged: (String) -> Unit,
    onDescriptionChanged: (String) -> Unit,
    onDeleteConfirmed: () -> Unit,
    onBackPressed: () -> Unit,
) {
    LaunchedEffect(key1 = uiState.diary!!.mood) {
        pagerState.scrollToPage(Mood.valueOf(uiState.diary.mood).ordinal)
    }
    Scaffold(
        topBar = {
            WriteTopBar(
                onBackPressed = onBackPressed,
                selectedDiary = null,
                onDeleteConfirmed = onDeleteConfirmed,
                moodName = moodName,
            )
        },
        content = {
            WriteContent(
                paddingValues = it,
                pagerState = pagerState,
                title = uiState.diary.title,
                description = uiState.diary.description,
                onTitleChanged = onTitleChanged,
                onDescriptionChanged = onDescriptionChanged
            )
        }
    )
}