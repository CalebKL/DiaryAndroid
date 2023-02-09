package com.example.diaryandroid.presentation.write

import Mood
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import com.example.diaryandroid.presentation.write.components.WriteContent
import com.example.diaryandroid.presentation.write.components.WriteTopBar
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.rememberPagerState
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import io.realm.kotlin.types.ObjectId
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.*

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPagerApi::class)
@Composable
@Destination
fun WriteScreen(
    title: String? = null,
    diaryId: ObjectId? = null,
    description:String?= null,
    navigator: DestinationsNavigator?,
    viewModel: WriteViewModel = viewModel(),
) {
    val state = viewModel.uiState
    val pagerState = rememberPagerState()
    val scrollState = rememberScrollState()
    val pageNumber by remember { derivedStateOf { pagerState.currentPage } }
    Scaffold(
        topBar = {
                 WriteTopBar(
                     onBackPressed = {
                         navigator?.popBackStack()
                     },
                     selectedDiary =null,
                     onDeleteConfirmed = {},
                     moodName = {Mood.values()[pageNumber].name}
                 )
        },
        content = {
            LaunchedEffect(key1 = Unit){
                viewModel.onGetDiaryDetails(diaryId = diaryId)
            }
            LaunchedEffect(key1 = Mood.Neutral) {
                pagerState.scrollToPage(Mood.valueOf(Mood.Neutral.name).ordinal)
            }
            WriteContent(
                paddingValues = it,
                scrollState = scrollState,
                pagerState = pagerState,
                title = title,
                description = description,
                onTitleChanged = {},
                onDescriptionChanged = {}
            )
        }
    )
}