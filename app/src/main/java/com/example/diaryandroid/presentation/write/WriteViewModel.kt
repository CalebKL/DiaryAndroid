package com.example.diaryandroid.presentation.write

import Mood
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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
    var uiState by mutableStateOf(UiState())
        private set
    private val diaryId= MutableStateFlow<ObjectId?>(ObjectId.create())

    fun updateDiaryId(id: ObjectId){
        diaryId.value = id
    }
    fun resetDiaryId(){
        diaryId.value = null
    }
    init {
        fetchSelectedDiary()
    }
    private fun fetchSelectedDiary(){
        if (uiState.selectedDiaryId != null) {
            val result = MongoDB.getSelectedDiary(
                diaryId =ObjectId.from(uiState.selectedDiaryId!!))
            if (result is Resource.Success){
                setSelectedDiary(diary = result.data!!)
                setTitle(title = result.data.title)
                setDescription(description = result.data.description )
                setMood(mood = Mood.valueOf(result.data.mood))
            }
        }
    }
    private fun setSelectedDiary(diary: Diary) {
        uiState = uiState.copy(selectedDiary = diary)
    }

    fun setTitle(title: String) {
        uiState = uiState.copy(title = title)
    }

    fun setDescription(description: String) {
        uiState = uiState.copy(description = description)
    }

    private fun setMood(mood: Mood) {
        uiState = uiState.copy(mood = mood)
    }

}
data class UiState(
    val selectedDiaryId: String? = null,
    val selectedDiary: Diary? = null,
    val title: String = "",
    val description: String = "",
    val mood: Mood = Mood.Neutral,
)