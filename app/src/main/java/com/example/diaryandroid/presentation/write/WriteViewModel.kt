package com.example.diaryandroid.presentation.write

import Mood
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.diaryandroid.data.MongoDB
import com.example.diaryandroid.model.Diary
import com.example.diaryandroid.util.Constants.WRITE_SCREEN_ARGUMENT_KEY
import com.example.diaryandroid.util.Resource
import io.realm.kotlin.types.ObjectId
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WriteViewModel(
    private val savedStateHandle: SavedStateHandle
):ViewModel() {

    var uiState by mutableStateOf(UiState())
        private set

    init {
        getDiaryIdArgument()
        fetchSelectedDiary()
    }
    private fun getDiaryIdArgument() {
        uiState = uiState.copy(
            selectedDiaryId = savedStateHandle.get<String>(
                key = WRITE_SCREEN_ARGUMENT_KEY
            )
        )
    }
    private fun fetchSelectedDiary() {
        if (uiState.selectedDiaryId != null){
            viewModelScope.launch(Dispatchers.Main){
                val diary = MongoDB.getSelectedDiary(
                    diaryId = ObjectId.Companion.from(uiState.selectedDiaryId!!)
                )
                if (diary is Resource.Success){
                    setSelectedDiary(diary.data)
                    setTitle(diary.data.title)
                    setDescription(diary.data.description)
                    setMood(mood = Mood.valueOf(diary.data.mood))
                }
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
    fun insertDiary(
        diary: Diary,
        onSuccess:()->Unit,
        onError:(String)->Unit
    ){
        viewModelScope.launch (Dispatchers.IO){
            val result = MongoDB.addNewDiary(diary = diary)
            if (result is Resource.Success){
                withContext(Dispatchers.Main){
                    onSuccess()
                }
            }else if (result is Resource.Error){
                withContext(Dispatchers.Main){
                    onError(result.error.message.toString())
                }
            }
        }
    }


}
data class UiState(
    val selectedDiaryId: String? = null,
    val selectedDiary: Diary? = null,
    val title: String = "",
    val description: String = "",
    val mood: Mood = Mood.Neutral,

)