package com.example.diaryandroid.data

import com.example.diaryandroid.model.Diary
import com.example.diaryandroid.util.Resource
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

typealias Diaries = Resource<Map<LocalDate, List<Diary>>>

interface MongoRepository {
    fun configureTheRealm()
    fun getAllDiaries():Flow<Diaries>
}