package com.example.diaryandroid.data

import com.example.diaryandroid.model.Diary
import com.example.diaryandroid.util.Resource
import io.realm.kotlin.types.ObjectId
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

typealias Diaries = Resource<Map<LocalDate, List<Diary>>>

interface MongoRepository {
    fun configureTheRealm()
    fun getAllDiaries():Flow<Diaries>
    fun getSelectedDiary(diaryId: ObjectId): Flow<Resource<Diary>>
    suspend fun insertDiary(diary: Diary): Resource<Diary>
    suspend fun updateDiary(diary: Diary): Resource<Diary>
    suspend fun deleteDiary(id:ObjectId): Resource<Boolean>
    suspend fun deleteAllDiaries():Resource<Boolean>
}