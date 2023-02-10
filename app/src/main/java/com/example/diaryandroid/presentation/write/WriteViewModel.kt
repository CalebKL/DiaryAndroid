package com.example.diaryandroid.presentation.write

import Mood
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.diaryandroid.data.MongoDB
import com.example.diaryandroid.model.Diary
import com.example.diaryandroid.util.Resource
import io.realm.kotlin.types.ObjectId
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class WriteViewModel:ViewModel() {
    var moodState = mutableStateOf(Mood.Neutral)
    var uiState = mutableStateOf(UiState())
        private set

    fun onGetDiaryDetails(diaryId:ObjectId?){
        if (diaryId != null) {
            MongoDB.getSelectedDiary(diaryId = diaryId).let {result->
                when(result){
                    is Resource.Loading ->{
                        uiState.value = UiState(isLoading = true)
                    }
                    is Resource.Success->{
                        uiState.value = UiState(diary = result.data)
                        moodState.value = Mood.valueOf(result.data!!.mood)
                    }
                    is Resource.Error ->{
                        uiState.value = UiState(error = result.message!!)
                    }
                }
            }
        }
    }
}
data class UiState(
    val isLoading: Boolean = false,
    val diary: Diary? = null,
    val error: String = "",
)