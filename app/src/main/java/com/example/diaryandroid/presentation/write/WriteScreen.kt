package com.example.diaryandroid.presentation.write

import android.net.Uri
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.example.diaryandroid.model.Diary
import com.example.diaryandroid.model.GalleryState
import com.example.diaryandroid.presentation.write.components.WriteContent
import com.example.diaryandroid.presentation.write.components.WriteTopBar
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import java.time.ZonedDateTime

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
    onSaveClicked:(Diary)->Unit,
    galleryState: GalleryState,
    onDateTimeUpdated:(ZonedDateTime)->Unit,
    onImageSelect:(Uri)->Unit

) {
    LaunchedEffect(key1 = uiState.mood) {
        pagerState.scrollToPage(Mood.valueOf(uiState.mood.name).ordinal)
    }
    Scaffold(
        topBar = {
            WriteTopBar(
                onBackPressed = onBackPressed,
                selectedDiary = uiState.selectedDiary,
                onDeleteConfirmed = onDeleteConfirmed,
                moodName = moodName,
                onDateTimeUpdated = onDateTimeUpdated
            )
        },
        content = {
            WriteContent(
                galleryState = galleryState,
                uiState = uiState,
                paddingValues = it,
                pagerState = pagerState,
                title = uiState.title,
                description = uiState.description,
                onTitleChanged = onTitleChanged,
                onDescriptionChanged = onDescriptionChanged,
                onSaveClicked = onSaveClicked,
                onImageSelect = onImageSelect
            )
        }
    )
}