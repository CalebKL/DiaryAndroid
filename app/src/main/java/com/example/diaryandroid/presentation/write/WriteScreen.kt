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
    diaryId: String? = null,
    description:String?= null,
    navigator: DestinationsNavigator?,
    viewModel: WriteViewModel = viewModel(),
    mood:String? = null
) {
    val state = viewModel.moodState
    val pagerState = rememberPagerState()
    val scrollState = rememberScrollState()
    val pageNumber by remember { derivedStateOf { pagerState.currentPage } }
    viewModel.onGetDiaryDetails()

    Scaffold(
        topBar = {
            WriteTopBar(
                onBackPressed = {
                    navigator?.popBackStack()
                    viewModel.resetDiaryId()
                },
                selectedDiary =null,
                onDeleteConfirmed = {},
                moodName = {Mood.values()[pageNumber].name},
            )
        },
        content = {
            val moodState = viewModel.moodState.collectAsState()
//            LaunchedEffect(key1 = Unit){
//                viewModel.onGetDiaryDetails(diaryId = diaryId)
//            }
            LaunchedEffect(key1 = moodState) {
                pagerState.scrollToPage(Mood.valueOf(moodState.value.name).ordinal)
            }
            WriteContent(
                paddingValues = it,
                scrollState = scrollState,
                pagerState = pagerState,
                title = title,
                description = description,
                onTitleChanged = {},
                onDescriptionChanged = {},
                onResetButton = {
                    viewModel.resetDiaryId()
                }
            )
        }
    )
}