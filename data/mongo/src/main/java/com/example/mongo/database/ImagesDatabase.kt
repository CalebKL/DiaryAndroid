package com.example.mongo.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.mongo.database.entity.ImageToDelete
import com.example.mongo.database.entity.ImageToUpload

@Database(
    entities = [ImageToUpload::class, ImageToDelete::class],
    version = 3,
    exportSchema = false
)
abstract class ImagesDatabase:RoomDatabase() {
    abstract fun imagesToUploadDao(): ImageToUploadDao
    abstract fun imageToDeleteDao(): ImagesToDeleteDao
}