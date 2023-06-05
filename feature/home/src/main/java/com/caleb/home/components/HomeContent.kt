package com.caleb.home.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.caleb.util.DiaryHolder
import com.caleb.util.model.Diary
import com.caleb.util.common.NoMatchFound
import java.time.LocalDate
import com.example.home.R

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun HomeContent(
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
        EmptyPage()
    }
}
@Composable
fun EmptyPage(
    title: String = "Empty Diary",
    subtitle: String = "Write Something"
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(all = 24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            style = TextStyle(
                fontSize = MaterialTheme.typography.titleMedium.fontSize,
                fontWeight = FontWeight.Medium
            )
        )
        Text(
            text = subtitle,
            style = TextStyle(
                fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                fontWeight = FontWeight.Normal
            )
        )
    }
}