package com.example.mongo.repository

import com.example.util.model.Diary
import com.example.util.model.Resource
import io.realm.kotlin.types.ObjectId
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.ZonedDateTime

typealias Diaries = Resource<Map<LocalDate, List<Diary>>>

internal interface MongoRepository {
    fun configureTheRealm()
    fun getAllDiaries():Flow<Diaries>
    fun getFilteredDiaries(zonedDateTime: ZonedDateTime):Flow<Diaries>
    fun getSelectedDiary(diaryId: ObjectId): Flow<Resource<Diary>>
    suspend fun insertDiary(diary: Diary): Resource<Diary>
    suspend fun updateDiary(diary: Diary): Resource<Diary>
    suspend fun deleteDiary(id:ObjectId): Resource<Boolean>
    suspend fun deleteAllDiaries():Resource<Boolean>
}