package com.example.diaryandroid.presentation.write

import Mood
import androidx.lifecycle.ViewModel
import com.example.diaryandroid.data.MongoDB
import com.example.diaryandroid.model.Diary
import com.example.diaryandroid.util.Resource
import io.realm.kotlin.types.ObjectId
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class WriteViewModel:ViewModel() {

    private val _state: MutableStateFlow<UiState> = MutableStateFlow(UiState())
    var state: StateFlow<UiState> = _state.asStateFlow()
        private set


    fun fetchSelectedDiary(id: String) {
        when(val result = MongoDB.getSelectedDiary(diaryId = ObjectId.Companion.from(id))){
            is Resource.Success ->{
                _state.value = UiState(diary =  result.data)
                setMood(Mood.valueOf(result.data!!.mood))
                setTitle(result.data.title)
                setDescription(result.data.description)
            }
            is Resource.Loading ->{
                _state.value = UiState(isLoading = true)
            }
            is Resource.Error ->{
                _state.value = UiState(error = result.message!!)
            }
        }
    }

    fun setTitle(title: String) {
        _state.value = UiState(diary = Diary().apply {
            this.title = title
        })
    }

    fun setDescription(description: String) {
        _state.value = UiState(diary = Diary().apply {
            this.description = description
        })
    }

    private fun setMood(mood: Mood) {
        _state.value = UiState(diary = Diary().apply {
            this.mood = Mood.valueOf(mood.name).toString()
        })
    }


}
data class UiState(
    val isLoading: Boolean = false,
    val diary: Diary? = null,
    val error: String = ""
)