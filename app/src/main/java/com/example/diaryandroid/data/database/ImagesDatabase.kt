package com.example.diaryandroid.data.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [ImageToUploadDao::class],
    version = 1,
    exportSchema = false
)
abstract class ImagesDatabase:RoomDatabase() {
    abstract fun imagesToUploadDao():ImageToUploadDao
}