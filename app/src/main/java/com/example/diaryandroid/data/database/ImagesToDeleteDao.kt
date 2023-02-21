package com.example.diaryandroid.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.diaryandroid.data.database.entity.ImageToDelete

@Dao
interface ImagesToDeleteDao {
    @Query("SELECT * FROM IMAGE_TO_DELETE_TABLE ORDER BY id ASC")
    suspend fun getAllImages(): List<ImageToDelete>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addImageToDelete(imageToDelete: ImageToDelete)

    @Query("DELETE FROM IMAGE_TO_DELETE_TABLE WHERE id=:imageId")
    suspend fun cleanupImage(imageId: Int)
}