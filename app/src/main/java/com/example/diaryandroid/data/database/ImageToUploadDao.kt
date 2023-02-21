package com.example.diaryandroid.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.diaryandroid.data.database.entity.ImageToUpload


@Dao
interface ImageToUploadDao {

    @Query("SELECT * FROM IMAGES_TO_UPLOAD_TABLE ORDER BY id ASC")
    suspend fun getAllImages(): List<ImageToUpload>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addImageToUpload(imageToUpload: ImageToUpload)

    @Query("DELETE FROM IMAGES_TO_UPLOAD_TABLE WHERE id=:imageId")
    suspend fun cleanupImage(imageId: Int)

}