package com.example.diaryandroid.presentation.home.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.example.diaryandroid.R
import com.example.diaryandroid.model.Diary
import com.example.diaryandroid.presentation.common.NoMatchFound
import java.time.LocalDate

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeContent(
    paddingValues: PaddingValues,
    diaryNotes:Map<LocalDate, List<Diary>>,
    onClick:(String)->Unit
) {
    if (diaryNotes.isNotEmpty()){
        LazyColumn(modifier = Modifier
            .padding(horizontal = 24.dp)
            .navigationBarsPadding()
            .padding(top = paddingValues.calculateTopPadding())
        ){
            diaryNotes.forEach{(localDate, diaries)->
                stickyHeader(key = localDate){
                    DateHeader(localDate = localDate)
                }
                items(
                    items = diaries,
                    key = {it._id.toString()}
                ){
                    DiaryHolder(diary = it, onClick = onClick)
                }
            }
        }
    }else{
        NoMatchFound(lottie = R.raw.no_match_found_dark)
    }
}