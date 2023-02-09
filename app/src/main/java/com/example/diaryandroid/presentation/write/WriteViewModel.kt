package com.example.diaryandroid.presentation.write

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.diaryandroid.data.MongoDB
import com.example.diaryandroid.presentation.write.state.UiState
import com.example.diaryandroid.util.Resource
import io.realm.kotlin.types.ObjectId
import kotlinx.coroutines.launch

class WriteViewModel:ViewModel() {

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
                    }
                    is Resource.Error ->{
                        uiState.value = UiState(error = result.message!!)
                    }
                }
            }
        }
    }
}