package com.example.diaryandroid.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.diaryandroid.util.Constants.IMAGES_TO_UPLOAD_TABLE

@Entity(tableName = IMAGES_TO_UPLOAD_TABLE)
data class ImageToUpload(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val remoteImagePath: String,
    val imageUri: String,
    val sessionUri: String
)