package com.example.diaryandroid.presentation.write.state

import com.example.diaryandroid.model.Diary

data class UiState(
    val isLoading: Boolean = false,
    val diary: Diary? = null,
    val error: String = "",
)
