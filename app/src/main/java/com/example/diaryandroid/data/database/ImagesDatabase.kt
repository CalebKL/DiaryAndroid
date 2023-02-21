package com.example.diaryandroid.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.diaryandroid.data.database.entity.ImageToUpload

@Database(
    entities = [ImageToUpload::class],
    version = 1,
    exportSchema = false
)
abstract class ImagesDatabase:RoomDatabase() {
    abstract fun imagesToUploadDao():ImageToUploadDao
}