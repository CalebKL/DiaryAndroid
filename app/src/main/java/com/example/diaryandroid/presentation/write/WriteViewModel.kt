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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber


class WriteViewModel:ViewModel() {
    private val _moodState: MutableStateFlow<Mood> = MutableStateFlow(Mood.Neutral)
    var moodState: StateFlow<Mood> = _moodState.asStateFlow()
        private set
    var uiState = mutableStateOf(UiState())
        private set
    private val diaryId= MutableStateFlow<ObjectId?>(ObjectId.create())

    fun updateDiaryId(id: ObjectId){
        diaryId.value = id
    }
    fun resetDiaryId(){
        diaryId.value = null
    }
    fun onGetDiaryDetails(){
        if (diaryId.value != null) {
            MongoDB.getSelectedDiary(diaryId = diaryId.value!!).let {result->
                Timber.e("mood_result $result")
                when(result){
                    is Resource.Loading ->{
                        uiState.value = UiState(isLoading = true)
                    }
                    is Resource.Success->{
                        uiState.value = UiState(diary = result.data)
                        _moodState.value = Mood.valueOf(result.data!!.mood)
                        Timber.e("moodSuccess$result")
                    }
                    is Resource.Error ->{
                        uiState.value = UiState(error = result.message!!)
                        Timber.e("mood error$result")
                    }
                }
            }
        }else{
            val y = 1+2
        }
    }
}
data class UiState(
    val isLoading: Boolean = false,
    val diary: Diary? = null,
    val error: String = "",
)