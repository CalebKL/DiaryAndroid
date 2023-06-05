package com.caleb.mongo.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.caleb.mongo.database.entity.ImageToDelete
import com.caleb.mongo.database.entity.ImageToUpload

@Database(
    entities = [ImageToUpload::class, ImageToDelete::class],
    version = 3,
    exportSchema = false
)
abstract class ImagesDatabase:RoomDatabase() {
    abstract fun imagesToUploadDao(): ImageToUploadDao
    abstract fun imageToDeleteDao(): ImagesToDeleteDao
}