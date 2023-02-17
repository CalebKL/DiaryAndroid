package com.example.diaryandroid.presentation.home

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.diaryandroid.data.Diaries
import com.example.diaryandroid.data.MongoDB
import com.example.diaryandroid.util.Resource
import kotlinx.coroutines.launch

class HomeViewModel:ViewModel() {
    var diaries: MutableState<Diaries> = mutableStateOf(Resource.Idle)
    init {
        observeAllDiaries()
    }
    private fun observeAllDiaries(){
       viewModelScope.launch {
           MongoDB.getAllDiaries().collect{result->
               diaries.value = result
           }
       }
    }
}