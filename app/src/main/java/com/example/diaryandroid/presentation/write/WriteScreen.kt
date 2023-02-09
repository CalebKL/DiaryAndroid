package com.example.diaryandroid.presentation.write

import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import com.example.diaryandroid.model.Diary
import com.example.diaryandroid.presentation.write.components.WriteContent
import com.example.diaryandroid.presentation.write.components.WriteTopBar
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.rememberPagerState
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPagerApi::class)
@Composable
@Destination
fun WriteScreen(
    navigator: DestinationsNavigator?
) {
    val pagerState = rememberPagerState()
    val scrollState = rememberScrollState()
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
            WriteContent(
                paddingValues = it,
                scrollState = scrollState,
                pagerState = pagerState,
                title = "",
                onTitleChanged = {},
                onDescriptionChanged = {}
            )
        }
    )
}