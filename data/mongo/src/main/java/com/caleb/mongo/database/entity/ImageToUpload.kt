package com.caleb.mongo.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.caleb.util.Constants.IMAGES_TO_UPLOAD_TABLE

@Entity(tableName = IMAGES_TO_UPLOAD_TABLE)
data class ImageToUpload(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val remoteImagePath: String,
    val imageUri: String,
    val sessionUri: String
)